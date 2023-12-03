package com.atom.adventofcode.services;

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

    /**
     * Get the source code for a given problem via GitHub
     * @param problem
     * @return
     */
    public String getCode(String problem) {
        StringBuilder sb = new StringBuilder();

        sb.append("This is a place holder.\n");
        sb.append("The intention is to link off to a github page (iframe\n\n");
        sb.append("Problem: ");
        sb.append(problem);
        sb.append("\n\n");

        return sb.toString();
    }
}
