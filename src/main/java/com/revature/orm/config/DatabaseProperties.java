package com.revature.orm.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DatabaseProperties {

    private final Path PROPERTIES_FILE_LOCATION = Paths.get("src/main/resources/orm.database.properties");

    private final Map<String, String> PROPERTIES = new HashMap<>();

    private DatabaseProperties() throws IOException {getProperties();}

    private static DatabaseProperties instance;

    public static DatabaseProperties getInstance(){
        try {
            return Optional.ofNullable(instance).orElse(instance = new DatabaseProperties());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't get an instance, check orm.database.properties");
        }
    }

    public String getPropertyByKey(String key) {
        return Optional.of(PROPERTIES.get(key)).get();
    }

    /**
     Gather the properties from the properties file and add them to the map.
     */
    private void getProperties() throws IOException {
        List<String> lines = Files.readAllLines(PROPERTIES_FILE_LOCATION);
        lines.forEach((String line) -> {
            String[] splits = line.split("=");
            PROPERTIES.put(splits[0], splits[1].replace("\"",""));
        });
    }

    // quickly return the preamble profile based on Config
    @SuppressWarnings("SameReturnValue")
    public String getProfile(){
        return "com.revature.orm";
    }

    public String getSchema(){
        return getPropertyByKey(getProfile()+".schema");
    }
}
