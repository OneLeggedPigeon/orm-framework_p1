package com.revature.orm.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabasePropertiesTest {

    @Test
    @DisplayName("getProfile")
    void getProfile() {
        assertEquals("com.revature.orm.dev", DatabaseProperties.getInstance().getProfile());
    }

    @Test
    void getSchema() {
        assertEquals("p_1", DatabaseProperties.getInstance().getSchema());
    }
}