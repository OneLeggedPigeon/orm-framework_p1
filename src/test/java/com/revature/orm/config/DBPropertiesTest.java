package com.revature.orm.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DBPropertiesTest {

    @Test
    @DisplayName("getProfile")
    void getProfile() {
        assertEquals("com.revature.orm.dev",DBProperties.getInstance().getProfile());
    }

    @Test
    void getSchema() {
        assertEquals("p_1",DBProperties.getInstance().getSchema());
    }
}