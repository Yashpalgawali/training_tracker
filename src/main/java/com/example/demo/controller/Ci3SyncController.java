package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Ci3SyncResult;
import com.example.demo.service.impl.Ci3SyncService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

/**
 * Exposes an on-demand endpoint to trigger a CI3 → local DB employee sync.
 *
 * Endpoint: POST /trainingtrackerrest/ci3/sync
 *
 * The scheduled sync runs automatically at midnight every day; this endpoint
 * is useful for manual triggers (e.g. after onboarding a new batch of employees
 * in CI3 and wanting to sync immediately without waiting for the nightly job).
 */
@RestController
@RequestMapping("ci3")
@RequiredArgsConstructor
public class Ci3SyncController {

    private static final Logger logger = LoggerFactory.getLogger(Ci3SyncController.class);

    private final Ci3SyncService ci3SyncService;

    /**
     * Triggers an immediate sync of employees from the CI3 application.
     *
     * @return sync result with counts of saved / skipped / failed records
     */
    @PostMapping("/sync")
    @Operation(
        summary     = "Sync employees from CI3",
        description = "Fetches all employees from the CI3 cPanel application via Basic Auth "
                    + "and saves any new records (deduped by empCode) into the local database."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sync completed successfully"),
        @ApiResponse(responseCode = "500", description = "Sync failed — check server logs for details")
    })
    public ResponseEntity<Ci3SyncResult> syncFromCi3() {
        logger.info("On-demand CI3 sync triggered via REST endpoint");
        try {
            Ci3SyncResult result = ci3SyncService.syncEmployeesFromCi3();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("On-demand CI3 sync failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
