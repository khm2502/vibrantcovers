package com.vibrantcovers.util;

import java.util.UUID;

public class IdGenerator {
    // Simple ID generator - you can replace with CUID if needed
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

