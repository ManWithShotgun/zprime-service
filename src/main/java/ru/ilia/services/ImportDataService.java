package ru.ilia.services;

import java.io.IOException;

public interface ImportDataService {

    void importData(String ksi, String events, String cycles) throws IOException;
}
