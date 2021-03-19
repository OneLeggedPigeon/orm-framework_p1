package com.revature.orm.config;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DBPropertiesTest {

    @Test
    void getInstance() {
    }

    @Test
    void getPropertyByKey() {
    }

    @Test
    @DisplayName("getProfile")
    void getProfile() throws IOException {
        assertEquals("com.revature.orm.dev",DBProperties.getInstance().getProfile());
    }

    @Test
    void getSchema() {
        assertEquals("p_1",DBProperties.getInstance().getSchema());
    }
}