package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D19 {

    private static final String testInput = """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}
                        
            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
            """;

    record Item(Map<String, Integer> values) {}
    record Data(Map<String, Workflow> workflows, List<Item> items) {}
    record Rule(String term, Character operator, int value, String workflow) {}
    record Workflow(String name, List<Rule> rules) {}

    private static Data parseInput(String input) {
        String[] split = input.split("\n\n");
        Map<String, Workflow> workflows = split[0].lines().map(D19::parseWorkflow)
                .collect(Collectors.toMap(Workflow::name, Function.identity()));
        List<Item> items = split[1].lines().map(D19::parseItems).toList();
        return new Data(workflows, items);
    }

    private static Item parseItems(String input) {
        input = input.replace("{", "").replace("}", "");
        String[] split = input.split(",");
        int x = Integer.parseInt(split[0].substring(2));
        int m = Integer.parseInt(split[1].substring(2));
        int a = Integer.parseInt(split[2].substring(2));
        int s = Integer.parseInt(split[3].substring(2));
        return new Item(Map.of("x", x, "m", m, "a", a, "s", s));
    }

    private static Workflow parseWorkflow(String line) {
        String name = line.substring(0, line.indexOf("{"));
        String[] split = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
        return new Workflow(name, Arrays.stream(split)
                .map(D19::parseRule)
                .toList());
    }

    private static Rule parseRule(String rule) {
        // regex
        String[] split = rule.split(":");
        int gt = split[0].indexOf(">");
        int lt = split[0].indexOf("<");

        if (gt == -1 && lt == -1) {
            return new Rule(split[0], null, 0, null);
        }
        String a = split[0].substring(0, Math.max(lt, gt));
        int b = Integer.parseInt(split[0].substring(Math.max(lt, gt) + 1));

        return new Rule(a, split[0].charAt(Math.max(lt, gt)), b, split[1]);
    }

    private static final Map<Character, BiFunction<Integer, Integer, Boolean>> operatorMap =
            Map.of('>', (a, b) -> a > b,
                    '<', (a, b) -> a < b);

    private static String resolveWorkflow(Workflow workflow, Item item) {
        for (Rule rule : workflow.rules()) {
            if (rule.operator() == null)
                return rule.term();

            if(operatorMap.get(rule.operator).apply(item.values().get(rule.term()), rule.value))
                return rule.workflow;
        }
        throw new RuntimeException("Should not get here");
    }

    private static long resolve(Map<String, Workflow> workflows, Item item) {
        long count = 0;

        Workflow wf = workflows.get("in");
        while (wf != null) {
            String res = resolveWorkflow(wf, item);
            switch (res) {
                case "A" -> {
                    count += item.values().values().stream().mapToLong(Long::valueOf).sum();
                    wf = null;
                }
                case "R" -> wf = null;
                default -> wf = workflows.get(res);
            }
        }
        return count;
    }

    private static long resolveItems(Map<String, Workflow> workflows, List<Item> items) {
        return items.stream().mapToLong(i -> resolve(workflows, i)).sum();
    }

    @Test
    public void partOne() {
        Data data = parseInput(testInput);
        assertEquals(7540, resolve(data.workflows(), data.items().get(0)));
        assertEquals(19114L, resolveItems(data.workflows(), data.items()));

        Data data2 = parseInput(FileReader.readFileString("src/test/resources/2023/D19.txt"));
        assertEquals(575412, resolveItems(data2.workflows(), data2.items()));
    }

    private long distinctCombination() {
        return 0;
    }

    @Test
    public void partTwo() {
        Data data = parseInput(testInput);
        assertEquals(167409079868000L, distinctCombination());
    }
}