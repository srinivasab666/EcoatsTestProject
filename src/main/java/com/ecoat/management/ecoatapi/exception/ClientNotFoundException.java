package com.ecoat.management.ecoatapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specified Client does not exist")
public class ClientNotFoundException extends RuntimeException {

}
