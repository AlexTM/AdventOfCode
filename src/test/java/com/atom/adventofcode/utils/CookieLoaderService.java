package com.atom.adventofcode.utils;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Read cookie from file in home directory
 */
public class CookieLoaderService {

    private static final String cookieFile = "~/.adventofcode.txt";

    public static String getCookie() throws Exception {
        Path filePath = Path.of(cookieFile);
        if(!filePath.toFile().exists()) {
            throw new RuntimeException("cookie file does not exist");
        }
        return Files.readString(filePath);
    }
}
