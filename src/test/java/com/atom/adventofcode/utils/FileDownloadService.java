package com.atom.adventofcode.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadService {
    private static final Logger logger = LoggerFactory.getLogger(FileDownloadService.class);

    private static final String PATH = "https://adventofcode.com/%s/day/%s";
    private static final String PATH_INPUT = "https://adventofcode.com/%s/day/%s/input";
    private static final String DOWNLOAD_LOCATION = "src/main/resources/y%s/day/%s";

    // FIXME remove this and put into a cmd line arg or env arg
//    private static final String cookie = "";

    private static void createDirectory(String downloadLocation) throws IOException {
        Path path = Paths.get(downloadLocation);
        Files.createDirectories(path);
        logger.info("Directory created {}", downloadLocation);
    }

    public static void getDay(int year, int day) throws Exception {

        String downloadLocation = String.format(DOWNLOAD_LOCATION, year, day);

        // ensure directory exist
        if (Files.exists(Paths.get(downloadLocation))) {
            logger.info("direction exists, not downloading: {}", downloadLocation);
            return;
        }
        createDirectory(downloadLocation);

        downloadDay(
                String.format(PATH, year, day),
                downloadLocation+"/1.html"
                );
        downloadDay(
                String.format(PATH_INPUT, year, day),
                downloadLocation+"/1.txt"
        );
    }

    private static RestTemplate restTemplate = new RestTemplate();

    public static void downloadDay(String downloadPath, String downloadLocation) throws Exception {

        String cookie = CookieLoaderService.getCookie();

        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Cookie", "session="+cookie);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(downloadPath, HttpMethod.GET, httpEntity, String.class);

        try(PrintWriter printWriter = new PrintWriter(new FileWriter(downloadLocation))) {
            printWriter.print(responseEntity.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
