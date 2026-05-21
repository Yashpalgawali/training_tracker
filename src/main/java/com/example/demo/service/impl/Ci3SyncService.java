package com.example.demo.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.Ci3EmployeeResponse;
import com.example.demo.dto.Ci3SyncResult;
import com.example.demo.entity.Category;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeHistory;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.ICategoryService;
import com.example.demo.service.ICompanyService;
import com.example.demo.service.IEmployeeHistoryService;

import lombok.RequiredArgsConstructor;

/**
 * Fetches employees from the remote CI3 (CodeIgniter 3) application
 * hosted on cPanel and syncs them into the local database.
 *
 * <h3>How it works</h3>
 * <ol>
 *   <li>Calls the CI3 REST endpoint with HTTP Basic Auth.</li>
 *   <li>Deserializes the JSON array into {@link Ci3EmployeeResponse} DTOs.</li>
 *   <li>For each record: skips if {@code empCode} already exists, otherwise
 *       resolves designation / department / category from the local DB and
 *       saves a new {@link Employee} + its {@link EmployeeHistory}.</li>
 * </ol>
 *
 * <h3>Triggers</h3>
 * <ul>
 *   <li><b>On-demand</b>: {@code POST /ci3/sync} via {@code Ci3SyncController}</li>
 *   <li><b>Scheduled</b>: runs automatically at midnight every day
 *       (configurable via {@code ci3.sync.cron} in application.properties)</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class Ci3SyncService {

    private static final Logger logger = LoggerFactory.getLogger(Ci3SyncService.class);

    // ── injected repos / services ──────────────────────────────────────────

    private final EmployeeRepository        emprepo;
    private final DesignationRepository     desigrepo;
    private final DepartmentRepository      deptrepo;
    private final ICategoryService          categoryserv;
    private final ICompanyService           compserv;
    private final IEmployeeHistoryService   emphistserv;
    private final RestTemplate              restTemplate;

    // ── configuration (set in application.properties) ──────────────────────

    /**
     * Full URL of the CI3 employee list endpoint.
     * Example: https://yoursite.cpaneldomain.com/api/employees
     */
    @Value("${ci3.api.url}")
    private String ci3ApiUrl;

    /** Basic Auth username for the CI3 API. */
    @Value("${ci3.api.username}")
    private String ci3Username;

    /** Basic Auth password for the CI3 API. */
    @Value("${ci3.api.password}")
    private String ci3Password;

    // ── public API ──────────────────────────────────────────────────────────

    /**
     * Fetches all employees from the CI3 API and syncs them into the local DB.
     * This method is called by the controller (on-demand) and the scheduler.
     *
     * @return {@link Ci3SyncResult} with counts of saved / skipped / failed records
     */
    public Ci3SyncResult syncEmployeesFromCi3() {

        logger.info("CI3 sync started — endpoint: {}", ci3ApiUrl);

        // 1. Build Basic Auth header
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(ci3Username, ci3Password);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 2. Call the CI3 API
        Ci3EmployeeResponse[] responseArray;
        try {
            ResponseEntity<Ci3EmployeeResponse[]> response = restTemplate.exchange(
                    ci3ApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Ci3EmployeeResponse[].class);

            responseArray = response.getBody();

        } catch (HttpClientErrorException e) {
            // 4xx — bad credentials, wrong URL, etc.
            logger.error("CI3 sync failed — HTTP client error: {} {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("CI3 API returned client error: " + e.getStatusCode(), e);

        } catch (HttpServerErrorException e) {
            // 5xx — CI3 server is down / throwing errors
            logger.error("CI3 sync failed — HTTP server error: {} {}", e.getStatusCode(), e.getMessage());
            throw new RuntimeException("CI3 API returned server error: " + e.getStatusCode(), e);

        } catch (ResourceAccessException e) {
            // Connection timeout / DNS failure
            logger.error("CI3 sync failed — cannot reach CI3 server: {}", e.getMessage());
            throw new RuntimeException("Cannot reach CI3 API: " + e.getMessage(), e);
        }

        if (responseArray == null || responseArray.length == 0) {
            logger.warn("CI3 sync — API returned an empty response");
            return new Ci3SyncResult(0, 0, 0, 0);
        }

        List<Ci3EmployeeResponse> employees = Arrays.asList(responseArray);
        logger.info("CI3 sync — {} records fetched from CI3", employees.size());

        // 3. Process each record
        int saved = 0, skipped = 0, failed = 0;

        for (Ci3EmployeeResponse ci3Emp : employees) {
            try {
                boolean wasSaved = processOne(ci3Emp);
                if (wasSaved) saved++; else skipped++;
            } catch (Exception e) {
                failed++;
                logger.error("CI3 sync — failed to process empCode='{}': {}",
                        ci3Emp.getEmpCode(), e.getMessage(), e);
            }
        }

        Ci3SyncResult result = new Ci3SyncResult(employees.size(), saved, skipped, failed);
        logger.info("CI3 sync complete — {}", result);
        return result;
    }

    // ── scheduler ──────────────────────────────────────────────────────────

    /**
     * Scheduled automatic sync.
     * Default cron: every day at midnight (configurable in application.properties).
     *
     * <pre>ci3.sync.cron=0 0 0 * * *</pre>
     */
    @Scheduled(cron = "${ci3.sync.cron:0 0 0 * * *}")
    public void scheduledSync() {
        logger.info("CI3 scheduled sync triggered");
        try {
            Ci3SyncResult result = syncEmployeesFromCi3();
            logger.info("CI3 scheduled sync finished — {}", result);
        } catch (Exception e) {
            logger.error("CI3 scheduled sync encountered an error: {}", e.getMessage(), e);
        }
    }

    // ── private helpers ────────────────────────────────────────────────────

    /**
     * Processes a single CI3 employee record.
     *
     * @return {@code true} if the record was saved, {@code false} if skipped (duplicate)
     */
    @Transactional
    private boolean processOne(Ci3EmployeeResponse ci3Emp) {

        String empCode = ci3Emp.getEmpCode() == null ? "" : ci3Emp.getEmpCode().trim();

        if (empCode.isEmpty()) {
            logger.warn("CI3 sync — skipping record with null/empty empCode: {}", ci3Emp);
            return false;
        }

        // Duplicate check — skip if already in local DB
        if (emprepo.findByEmpCode(empCode).isPresent()) {
            logger.debug("CI3 sync — empCode '{}' already exists, skipping", empCode);
            return false;
        }

        // ── Resolve lookup entities ──

        // Designation
        Designation desig = null;
        if (ci3Emp.getDesignation() != null && !ci3Emp.getDesignation().isBlank()) {
            desig = desigrepo.findByDesigName(ci3Emp.getDesignation().trim());
            if (desig == null) {
                logger.warn("CI3 sync — designation '{}' not found in local DB for empCode '{}'",
                        ci3Emp.getDesignation(), empCode);
            }
        }

        // Department (matched by dept name + company name)
        Department dept = null;
        if (ci3Emp.getDepartment() != null && !ci3Emp.getDepartment().isBlank()
                && ci3Emp.getCompany() != null && !ci3Emp.getCompany().isBlank()) {
            dept = deptrepo.getDepartmentByDeptNameAndCompanyName(
                    ci3Emp.getDepartment().trim(), ci3Emp.getCompany().trim());
            if (dept == null) {
                logger.warn("CI3 sync — department '{}' / company '{}' not found for empCode '{}'",
                        ci3Emp.getDepartment(), ci3Emp.getCompany(), empCode);
            }
        }

        // Category
        Category category = null;
        if (ci3Emp.getCategory() != null && !ci3Emp.getCategory().isBlank()) {
            category = categoryserv.getCategoryByCategoryName(ci3Emp.getCategory().trim());
            if (category == null) {
                logger.warn("CI3 sync — category '{}' not found in local DB for empCode '{}'",
                        ci3Emp.getCategory(), empCode);
            }
        }

        // ── Build and save Employee ──
        Employee emp = new Employee();
        emp.setEmpCode(empCode);
        emp.setEmpName(ci3Emp.getEmpName() != null ? ci3Emp.getEmpName().trim() : "");
        emp.setJoiningDate(ci3Emp.getJoiningDate() != null ? ci3Emp.getJoiningDate().trim() : "");
        emp.setContractorName(ci3Emp.getContractorName() != null ? ci3Emp.getContractorName().trim() : "");
        emp.setDesignation(desig);
        emp.setDepartment(dept);
        emp.setCategory(category);
        emp.setStatus(1); // Active by default

        Employee saved = emprepo.save(emp);

        // ── Build and save EmployeeHistory ──
        EmployeeHistory hist = new EmployeeHistory();
        hist.setEmployee(saved);
        hist.setEmpCode(saved.getEmpCode());
        hist.setEmpName(saved.getEmpName());
        hist.setJoiningDate(saved.getJoiningDate());
        hist.setContractorName(saved.getContractorName());
        hist.setStatus(saved.getStatus());
        hist.setLeaveDate("");
        hist.setCategory(category != null ? category.getCategory() : "");
        hist.setDesigName(desig  != null ? desig.getDesigName()  : "");

        if (dept != null) {
            hist.setDeptName(dept.getDeptName());
            hist.setCompName(dept.getCompany() != null ? dept.getCompany().getCompName() : "");
        } else {
            hist.setDeptName("");
            // Fallback: try to look up company directly if dept wasn't resolved
            if (ci3Emp.getCompany() != null && !ci3Emp.getCompany().isBlank()) {
                try {
                    var comp = compserv.getCompanyByName(ci3Emp.getCompany().trim());
                    hist.setCompName(comp != null ? comp.getCompName() : "");
                } catch (Exception ignored) {
                    hist.setCompName("");
                }
            } else {
                hist.setCompName("");
            }
        }

        emphistserv.saveEmployeeHistory(hist);

        logger.debug("CI3 sync — saved empCode='{}'", empCode);
        return true;
    }
}
