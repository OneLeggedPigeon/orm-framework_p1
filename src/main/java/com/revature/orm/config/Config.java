package com.revature.orm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Config {

    private final Path CONFIG_FILE_LOCATION = Paths.get("src/main/resources/orm.config");

    private final Map<String, String> PROPERTIES = new HashMap<>();

    private Config() throws IOException {getProperties();}

    private static Config instance;

    public static Config getInstance(){
        try {
            return Optional.ofNullable(instance).orElse(instance = new Config());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPropertyByKey(String key) {
        return Optional.of(PROPERTIES.get(key)).get();
    }

    /**
     Gather the properties from the config file and add them to the map.
     */
    private void getProperties() throws IOException {
        List<String> lines = Files.readAllLines(CONFIG_FILE_LOCATION);
        lines.forEach((String line) -> {
            String[] splits = line.split("=");
            PROPERTIES.put(splits[0], splits[1].replace("\"",""));
        });
    }
}
