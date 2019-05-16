package ru.ilia.controller.entities;

import java.util.Map;

public class WsPointsResponse {

    private final String ksi;
    private final String events;
    private final String cycles;
    private final Map<String, String> result;

    public WsPointsResponse(String ksi, String events, String cycles, Map<String, String> result) {
        this.ksi = ksi;
        this.events = events;
        this.cycles = cycles;
        this.result = result;
    }

    public String getKsi() {
        return ksi;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public String getEvents() {
        return events;
    }

    public String getCycles() {
        return cycles;
    }
}

