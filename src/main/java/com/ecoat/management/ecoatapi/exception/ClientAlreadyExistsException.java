package com.ecoat.management.ecoatapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Client already exists")
public class ClientAlreadyExistsException extends Exception{

}
