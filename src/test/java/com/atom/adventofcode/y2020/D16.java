package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

    record Range(int min, int max) {}
    record Rule(Range[] ranges){}

    private final static Pattern p = Pattern.compile(
            "(.*):\\s(\\d*)-(\\d*) or (\\d*)-(\\d*)");

    private Map<String, Rule> readRules(String filename) throws FileNotFoundException {
        Map<String, Rule> rules = new HashMap<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                String line = in.nextLine();
                if(line.isBlank()){
                    break;
                }
                Matcher m = p.matcher(line);
                if (m.find()) {
                    rules.put(m.group(1),
                            new Rule(new Range[]{
                                    new Range(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))),
                                    new Range(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)))}));
                }
            }
            in.close();
            return rules;
        }
    }

    private List<List<Integer>> readTickets(String filename) throws FileNotFoundException {
        List<List<Integer>> tickets = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            boolean read = false;
            while (in.hasNext()) {
                String line = in.nextLine();
                if(!read) {
                    if(line.startsWith("nearby tickets")) {
                        read = true;
                    }
                    continue;
                }
                String parts[] = line.split(",");
                List<Integer> ticket = new ArrayList<>();
                for(String part : parts)
                    ticket.add(Integer.parseInt(part));
                tickets.add(ticket);
            }
            in.close();
            return tickets;
        }
    }


    @Test
    public void testReadTicket() throws FileNotFoundException {
        Map<String, Rule> rules = readRules("src/test/resources/2020/D16.txt");
        List<List<Integer>> tickets = readTickets("src/test/resources/2020/D16.txt");

        int sum = 0;
        for(List<Integer> ticket : tickets)
            sum += scanTicketValidForAnyRange(rules, ticket);
        assertEquals(25895, sum);
    }


    @Test
    public void testReadTicketTest() throws FileNotFoundException {
        Map<String, Rule> rules = readRules("src/test/resources/2020/D16_t.txt");
        List<List<Integer>> tickets = readTickets("src/test/resources/2020/D16_t.txt");

        int sum = 0;
        for(List<Integer> ticket : tickets)
            sum += scanTicketValidForAnyRange(rules, ticket);
        assertEquals(71, sum);
    }

    @Test
    public void testTicketValidateTest() throws FileNotFoundException {
        final Map<String, Rule> rules = readRules("src/test/resources/2020/D16_t2.txt");
        final List<List<Integer>> tickets = readTickets("src/test/resources/2020/D16_t2.txt");

        final HashMap<String, Integer> fieldToColumnMap = testTicketValidate2(rules, tickets);

        System.out.println(fieldToColumnMap);
        assertEquals(0, fieldToColumnMap.get("row"));
        assertEquals(1, fieldToColumnMap.get("class"));
        assertEquals(2, fieldToColumnMap.get("seat"));
    }

    @Test
    public void testTicketValidateTest2() throws FileNotFoundException {
        final Map<String, Rule> rules = readRules("src/test/resources/2020/D16.txt");
        final List<List<Integer>> tickets = readTickets("src/test/resources/2020/D16.txt");

        final HashMap<String, Integer> fieldToColumnMap = testTicketValidate2(rules, tickets);

        long res = 1;
        final int[] myticket = new int[] {61,151,137,191,59,163,89,83,71,179,67,149,197,167,181,173,53,139,193,157};
        for (Map.Entry<String, Integer> e : fieldToColumnMap.entrySet()) {
            if(e.getKey().startsWith("departure")) {
                System.out.println(e.getKey()+" "+myticket[e.getValue()]);
                res *= myticket[e.getValue()];
            }
        }

        // NOT 4771599094597
        System.out.println("Result "+res);
    }

    @Test
    public HashMap<String, Integer> testTicketValidate2(
            final Map<String, Rule> rules, final List<List<Integer>> tickets
    ) {
        final int columns = tickets.get(0).size();

        final HashMap<String, Set<Integer>> failedRulesByColumns = new HashMap<>();
        tickets.stream()
                .filter(ticket -> scanTicketValidForAnyRange(rules, ticket) == 0)
                .forEach(ticket -> buildSetOfFailedRules(rules, ticket, failedRulesByColumns));

        final HashMap<String, Set<Integer>> validatedRulesByColumn = new HashMap<>();
        failedRulesByColumns.forEach((key, value) -> validatedRulesByColumn.put(key,
                IntStream.range(0, columns)
                        .filter(value::contains)
                        .boxed()
                        .collect(Collectors.toSet())));

        System.out.println(validatedRulesByColumn);
        //validatedRulesByColumn = failedRulesByColumns;


        // Start to reduce down starting from fields which only have 1 column
        HashMap<String, Integer> fieldToColumnMap = new HashMap<>();
        while (!validatedRulesByColumn.isEmpty()) {
            String key = null;
            for (Map.Entry<String, Set<Integer>> e : validatedRulesByColumn.entrySet()) {
                if (e.getValue().size() == 1) {
                    key = e.getKey();
                    break;
                }
            }
            if (key == null)
                break;
            int col = validatedRulesByColumn.get(key).iterator().next();
            validatedRulesByColumn.remove(key);
            fieldToColumnMap.put(key, col);
            // remove from all other
            validatedRulesByColumn.values().forEach(v -> v.remove(col));

        }
//        fieldToColumnMap.put("departure track", 1);
//        System.out.println(fieldToColumnMap);

        if(!validatedRulesByColumn.isEmpty()) {
            Collection<Integer> cols = fieldToColumnMap.values();
            for (int i = 0; i < columns; i++) {
                if (cols.contains(i)) {
                    fieldToColumnMap.put(
                            validatedRulesByColumn.keySet().iterator().next(), i);
                }
            }
        }

        return fieldToColumnMap;
    }

    private int scanTicketValidForAnyRange(Map<String, Rule> ruleMap, List<Integer> ticket) {
        int sum = 0;
        for (int i : ticket) {
            boolean inRange = false;
            for (Rule rule : ruleMap.values()) {
                if (i >= rule.ranges[0].min && i <= rule.ranges[0].max ||
                        i >= rule.ranges[1].min && i <= rule.ranges[1].max) {
                    inRange = true;
                    break;
                }
            }
            if (!inRange)
                sum += i;
        }
        return sum;
    }

    private void buildSetOfFailedRules(Map<String, Rule> ruleMap, List<Integer> ticket, HashMap<String, Set<Integer>> cache) {
        for(int col=0; col<ticket.size(); col++) {
            int i = ticket.get(col);
            for(Map.Entry<String, Rule> e : ruleMap.entrySet()) {
                Rule rule = e.getValue();
                if(i >= rule.ranges[0].min && i<=rule.ranges[0].max ||
                        i >= rule.ranges[1].min && i<=rule.ranges[1].max) {
                } else {
                    // false for this rule on column col
                    Set<Integer> c = cache.getOrDefault(e.getKey(), new HashSet<>());
                    c.add(col);
                    cache.put(e.getKey(), c);
                }
            }
        }
    }


}
