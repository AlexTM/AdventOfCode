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

/**
 * --- Day 16: Ticket Translation ---
 * As you're walking to yet another connecting flight, you realize that one of the legs of your re-routed trip coming
 * up is on a high-speed train. However, the train ticket you were given is in a language you don't understand. You
 * should probably figure out what it says before you get to the train station after the next flight.
 *
 * Unfortunately, you can't actually read the words on the ticket. You can, however, read the numbers, and so you
 * figure out the fields these tickets must have and the valid ranges for values in those fields.
 *
 * You collect the rules for ticket fields, the numbers on your ticket, and the numbers on other nearby tickets for
 * the same train service (via the airport security cameras) together into a single document you can reference (your
 * puzzle input).
 *
 * The rules for ticket fields specify a list of fields that exist somewhere on the ticket and the valid ranges of
 * values for each field. For example, a rule like class: 1-3 or 5-7 means that one of the fields in every ticket is
 * named class and can be any value in the ranges 1-3 or 5-7 (inclusive, such that 3 and 5 are both valid in this
 * field, but 4 is not).
 *
 * Each ticket is represented by a single line of comma-separated values. The values are the numbers on the ticket in
 * the order they appear; every ticket has the same format. For example, consider this ticket:
 *
 * .--------------------------------------------------------.
 * | ????: 101    ?????: 102   ??????????: 103     ???: 104 |
 * |                                                        |
 * | ??: 301  ??: 302             ???????: 303      ??????? |
 * | ??: 401  ??: 402           ???? ????: 403    ????????? |
 * '--------------------------------------------------------'
 * Here, ? represents text in a language you don't understand. This ticket might be represented as 101,102,103,104,
 * 301,302,303,401,402,403; of course, the actual train tickets you're looking at are much more complicated. In any
 * case, you've extracted just the numbers in such a way that the first number is always the same specific field, the
 * second number is always a different specific field, and so on - you just don't know what each position actually
 * means!
 *
 * Start by determining which tickets are completely invalid; these are tickets that contain values which aren't
 * valid for any field. Ignore your ticket for now.
 *
 * For example, suppose you have the following notes:
 *
 * class: 1-3 or 5-7
 * row: 6-11 or 33-44
 * seat: 13-40 or 45-50
 *
 * your ticket:
 * 7,1,14
 *
 * nearby tickets:
 * 7,3,47
 * 40,4,50
 * 55,2,20
 * 38,6,12
 * It doesn't matter which position corresponds to which field; you can identify invalid nearby tickets by
 * considering only whether tickets contain values that are not valid for any field. In this example, the values on
 * the first nearby ticket are all valid for at least one field. This is not true of the other three nearby tickets:
 * the values 4, 55, and 12 are are not valid for any field. Adding together all of the invalid values produces your
 * ticket scanning error rate: 4 + 55 + 12 = 71.
 *
 * Consider the validity of the nearby tickets you scanned. What is your ticket scanning error rate?
 *
 * Your puzzle answer was 25895.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 *
 * --- Part Two ---
 * Now that you've identified which tickets contain invalid values, discard those tickets entirely. Use the remaining
 * valid tickets to determine which field is which.
 *
 * Using the valid ranges for each field, determine what order the fields appear on the tickets. The order is
 * consistent between all tickets: if seat is the third field, it is the third field on every ticket, including your
 * ticket.
 *
 * For example, suppose you have the following notes:
 *
 * class: 0-1 or 4-19
 * row: 0-5 or 8-19
 * seat: 0-13 or 16-19
 *
 * your ticket:
 * 11,12,13
 *
 * nearby tickets:
 * 3,9,18
 * 15,1,5
 * 5,14,9
 * Based on the nearby tickets in the above example, the first position must be row, the second position must be
 * class, and the third position must be seat; you can conclude that in your ticket, class is 12, row is 11, and seat
 * is 13.
 *
 * Once you work out which field is which, look for the six fields on your ticket that start with the word departure.
 * What do you get if you multiply those six values together?
 */
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
