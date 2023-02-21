package com.atom.adventofcode.utils;

import com.atom.adventofcode.utils.html.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PageExtractMain {

    private static final String FILE_LOCATION = "src/main/resources/y%s/day/%s";

    public static void main(String[] args) throws IOException {

        String fileName = String.format(FILE_LOCATION, 2015, 1);

        Path filePath = Path.of(fileName+"/1.html");
        String content = Files.readString(filePath);

        Page page = PageExtractService.testPageExtract(2015, 1, content);

        PageExtractService.generatePage(page);
    }
}
