package com.ecoat.management.ecoatapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Employee Timesheet Type is different from the requested reporting")
public class InvalidReportingRequestedException extends Exception{
}
