package ru.ilia.controller.entities;

public class WsOnePointResponse {

    private final String ksi;
    private final String mass;
    private final String result;
    private final String events;
    private final String cycles;

    public WsOnePointResponse(String ksi, String mass, String events, String cycles, String result) {
        this.ksi = ksi;
        this.mass = mass;
        this.result = result;
        this.events = events;
        this.cycles = cycles;
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

    public String getEvents() {
        return events;
    }

    public String getCycles() {
        return cycles;
    }
}
