package ru.ilia.controller.entities;

public class WsOnePointResponse {

    private final String ksi;
    private final String mass;
    private final String result;

    public WsOnePointResponse(String ksi, String mass, String result) {
        this.ksi = ksi;
        this.mass = mass;
        this.result = result;
    }

    public String getKsi() {
        return ksi;
    }

    public String getMass() {
        return mass;
    }

    public String getResult() {
        return result;
    }
}
