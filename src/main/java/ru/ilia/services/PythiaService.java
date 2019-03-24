package ru.ilia.services;

public interface PythiaService {

    void calculate(PythiaRequest request);

    String getResultFromFile(PythiaRequest request);
}
