package com.atom.adventofcode;

import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class AocTest {

    private static final String path = "src/main/resources/";
    private ObjectMapper om = new ObjectMapper();

//    public String getPackageName() {
//        System.out.println(packageName);
//        return packageName;
//    }

    private String getResourcePath() {
        String packageName = this.getClass().getPackage().getName();
        String[] split = packageName.split("\\.");
        String resource = path+split[split.length-1]+"/"+this.getClass().getSimpleName()+".json";
        System.out.println(resource);
        return resource;
    }

    public Challenge<?, ?> getChallenge() throws JsonProcessingException {
        String content = FileReader.readFileForObject(getResourcePath(), new StringBuilder(), (a, b)-> b.append(a)).toString();
        return om.readValue(content, Challenge.class);
    }

//    public <T> getTestData() {
//
//    }

//    @Test
//    public void testPartA() throws Exception {
//        Challenge<?, ?> challenge = getChallenge();
//    }
//
//
//    @Test
//    public void testPartB() {
//
//    }
}
