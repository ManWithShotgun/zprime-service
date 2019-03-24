package ru.ilia.services;

public class PythiaRequest {

    private final String ksi;
    private final String mass;

    public PythiaRequest(String ksi, String mass) {
        this.ksi = ksi;
        this.mass = mass;
    }

    public String getKsi() {
        return ksi;
    }

    public String getMass() {
        return mass;
    }
}
