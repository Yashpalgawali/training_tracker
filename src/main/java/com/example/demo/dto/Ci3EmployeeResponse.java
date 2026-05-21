package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO that maps the JSON response returned by the CI3 (CodeIgniter 3) API.
 *
 * All fields use @JsonProperty so they survive any naming differences between
 * the CI3 snake_case convention and Java camelCase.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) ensures that any extra fields
 * the CI3 API sends in the future won't break deserialization.
 *
 * ── Replace the @JsonProperty values with the exact field names your CI3
 *    API returns once you have access to the real endpoint. ──
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ci3EmployeeResponse {

    /** Employee unique code — used as the deduplication key. */
    @JsonProperty("emp_code")
    private String empCode;

    @JsonProperty("emp_name")
    private String empName;

    /** Designation name as a plain string (will be looked up in local DB). */
    @JsonProperty("designation")
    private String designation;

    /** Department name as a plain string (will be looked up in local DB). */
    @JsonProperty("department")
    private String department;

    /** Company name as a plain string (will be looked up in local DB). */
    @JsonProperty("company")
    private String company;

    @JsonProperty("joining_date")
    private String joiningDate;

    @JsonProperty("contractor_name")
    private String contractorName;

    /** Category name as a plain string (will be looked up in local DB). */
    @JsonProperty("category")
    private String category;
}
