package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D12 {

    private final ObjectMapper mapper = new ObjectMapper();

    private int parseString(String json, boolean ignoreRed) {
        try {
            JsonNode jsonNode = mapper.readTree(json);
            return parse(jsonNode, ignoreRed);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int parse(JsonNode jsonNode, boolean ignoreRed) {

        int c=0;

        if(jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                c += parse(node, ignoreRed);
            }
        } else if(jsonNode.isNumber()) {
            return jsonNode.asInt();
        } else if(jsonNode.isObject()) {
            int tmpc = 0;
            for (JsonNode node : jsonNode) {
                if(ignoreRed && node.isTextual()) {
                    if(node.asText().equalsIgnoreCase("red")) {
                        tmpc = 0;
                        break;
                    }
                } else {
                    tmpc += parse(node, ignoreRed);
                }
            }
            c += tmpc;
        }

        return c;
    }

    @Test
    public void testAccounting() {

        assertEquals(156366,
                parseString(String.join("",
                        FileReader.readFileStringList("src/test/resources/2015/D12.txt")),
                        false
                ));

        assertEquals(96852,
                parseString(String.join("",
                                FileReader.readFileStringList("src/test/resources/2015/D12.txt")),
                        true
                ));
    }

}
