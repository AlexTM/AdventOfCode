package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D12 {

    private ObjectMapper mapper = new ObjectMapper();


    private int parseString(String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);

            return parse(jsonNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int parse(JsonNode jsonNode) {

        int c=0;

        if(jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                c += parse(node);
            }
//        } else if(jsonNode.isTextual()) {
//            String str = jsonNode.asText();
//            if(str.equalsIgnoreCase("red"))
//                return Integer.MIN_VALUE;
        } else if(jsonNode.isNumber()) {
            return jsonNode.asInt();
        } else if(jsonNode.isObject()) {
            ObjectNode on = (ObjectNode)jsonNode;
            if(on.get("red") != null)
                return 0;
            Iterator<String> it = on.fieldNames();
            while(it.hasNext()) {
                String name = it.next();
//                if(name.equalsIgnoreCase("red"))
//                    continue;
                c += parse(on.get(name));
            }

        }

        return c;
    }

    @Test
    public void testAccounting() {
//        assertEquals(0, parseString("{\"red\":2}"));


//        assertEquals(6, parseString("[1, 2, 3]"));
//        assertEquals(6, parseString("{\"a\":2,\"b\":4}"));
//        assertEquals(3, parseString("[[[3]]]"));
//        assertEquals(3, parseString("{\"a\":{\"b\":4},\"c\":-1}"));
//
        assertEquals(156366,
                parseString(String.join("",
                        FileReader.readFileStringList("src/test/resources/2015/D12.txt"))));
//
//        assertEquals(156366,
//                parseString(String.join("",
//                        FileReader.readFileStringList("src/test/resources/2015/D12.txt"))));
    }

    @Test
    public void testAccounting2() {
        assertEquals(6, parseString("[1, 2, 3]"));
        assertEquals(4, parseString("[1,{\"c\":\"red\",\"b\":2},3]"));
        assertEquals(0, parseString("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}"));
        assertEquals(6, parseString("[1,\"red\",5]"));
    }
}
