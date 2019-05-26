package ru.ilia.services;

import java.io.IOException;
import java.util.Map;

public interface ColisionExpectedWithLinesService {

    Map<String, String> getCollisions() throws IOException;
}
