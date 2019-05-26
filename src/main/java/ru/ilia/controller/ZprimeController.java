package ru.ilia.controller;

import org.springframework.http.ResponseEntity;

public interface ZprimeController {

    String getPoint(String ksi, String mass, String pythiaEvents, String countOfRecalculatePoint);
    String importData(String ksi, String pythiaEvents, String countOfRecalculatePoint);
    ResponseEntity<?> getResultCollisionPoints();

}
