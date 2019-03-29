package ru.ilia.exception;

public class PythiaCalculationException extends RuntimeException {
    public PythiaCalculationException(Exception e) {
        super(e);
    }
}
