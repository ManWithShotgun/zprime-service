package ru.ilia.services;

import org.apache.commons.lang3.StringUtils;
import ru.ilia.exception.PythiaResultException;

public class PythiaResult {

    private static final String RESULT_PREFIX = "Cross:";
    private final String rawString;

    public PythiaResult(String rawString) {
        this.rawString = rawString;
    }

    public String getResult() {
        if (StringUtils.isBlank(this.rawString)) {
            throw new PythiaResultException("Calculated result is blank");
        }
        if (!this.rawString.startsWith(RESULT_PREFIX)) {
            throw new PythiaResultException("Calculated result has incorrect format");
        }
        String[] split = this.rawString.split(RESULT_PREFIX);
        String value = split[1];
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new PythiaResultException("Calculated result has incorrect format", e);
        }
        return value;
    }
}
