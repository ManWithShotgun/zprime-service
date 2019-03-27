package ru.ilia.services;

public class PythiaRequest {

    private final String ksi;
    private final String mass;

    // ksi - MixingFactor
    // result - cross section * Branching <value> pb (K=1.9)
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
