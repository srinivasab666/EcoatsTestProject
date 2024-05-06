package com.ecoat.management.ecoatapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Corporate already exists")
public class CorporateAlreadyExistsException extends Exception {
}
