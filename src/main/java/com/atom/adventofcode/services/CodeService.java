package com.atom.adventofcode.services;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Service
public class CodeService {
    private static final Logger logger = LoggerFactory.getLogger(CodeService.class);

    @Autowired
    private ResourceLoader resourceLoader;


/*
    @Value("classpath*:**")
    private Resource[] resources;

    @PostConstruct
    public void test() throws IOException {
        for (final Resource res : resources) {
            if(Objects.requireNonNull(res.getFilename()).contains(".html"))
                System.out.println(res.getFilename());
        }
        for (final Resource res : resources) {
            if(Objects.requireNonNull(res.getFilename()).contains(".java"))
                System.out.println(res.getURI());
        }
    }
    */

    /**
     * FIXME, this need to fetch from the classpath
     * @return
     */
/*
    private String find() {

//        String loc = "file:/home/alex/development/AdventOfCode/src/main/java/com/atom/adventofcode/test2015/Day1.java";
//        String loc = "file:/home/alex/development/AdventOfCode/src/main/java/com/atom/adventofcode/y2014/D1.java";
//        String loc = "classpath:Day1.java";
//        String loc = "classpath:com/atom/adventofcode/test2015/Day1.java";
//        String loc = "classpath:2015/D2.txt";
//        String loc = "classpath:template.java";
//        String loc = "classpath:test2.txt";
        String loc = "classpath:/com/atom/adventofcode/PartOne.java";


        StringBuilder sb = new StringBuilder();
        try {
            Resource resource = resourceLoader.getResource(loc);
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            // Handle the exception
            logger.error("Something wrong", e);
        }
        return sb.toString();
    }
*/


    public String getCode(String problem) {
        StringBuilder sb = new StringBuilder();

        sb.append("This is a place holder.\n");
        sb.append("The intention is to load the source file off the classpath\n\n");
        sb.append("Problem: ");
        sb.append(problem);
        sb.append("\n\n");

        return sb.toString();
    }
}
