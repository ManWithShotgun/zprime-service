package ru.ilia.services;

import java.util.Map;

public interface ZprimeService {

    String getResult(final String ksi, final String mass, final String countOfEvents, final String countOfRecalculate);
    Map<String, String> getAllResults(final String ksi);
}
