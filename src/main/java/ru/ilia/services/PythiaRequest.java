package ru.ilia.services;

public class PythiaRequest {

    private final String ksi;
    private final String mass;
    private final String countOfEvents;
    private final String seed;

    // ksi - MixingFactor
    // result - cross section * Branching <value> pb (K=1.9)
    // countOfEvents - count of events which was generated during modulation process
    // seed - seed of random
    public PythiaRequest(String ksi, String mass, String countOfEvents, String seed) {
        this.ksi = ksi;
        this.mass = mass;
        this.countOfEvents = countOfEvents;
        this.seed = seed;
    }

    public String getKsi() {
        return ksi;
    }

    public String getMass() {
        return mass;
    }

    public String getCountOfEvents() {
        return countOfEvents;
    }

    public String getSeed() {
        return seed;
    }
}
