package ru.ilia.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Error")
    @ExceptionHandler(PythiaResultException.class)
    public void pythiaResultExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Error")
    @ExceptionHandler(PythiaCalculationException.class)
    public void pythiaCalculationExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage(), e);
    }
}
