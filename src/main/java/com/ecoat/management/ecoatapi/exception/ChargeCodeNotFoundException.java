package com.ecoat.management.ecoatapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "ChargeCode not found exists")
public class ChargeCodeNotFoundException extends Exception{
}
