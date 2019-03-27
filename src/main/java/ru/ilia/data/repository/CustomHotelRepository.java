package ru.ilia.data.repository;

import java.io.IOException;

public interface CustomHotelRepository {
    void createTable();
    void loadData() throws IOException;
}
