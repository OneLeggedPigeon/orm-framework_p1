package com.revature;

import dev.enterprise.logger.FileLogger;

import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException {
        FileLogger.getInstance();
    }
}
