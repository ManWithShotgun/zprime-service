package ru.ilia.exception;

public class PythiaResultException extends RuntimeException {
    public PythiaResultException(String s) {
        super(s);
    }

    public PythiaResultException(String message, RuntimeException e) {
        super(message, e);
    }
}
