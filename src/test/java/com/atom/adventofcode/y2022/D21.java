package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D21 {

    enum Operation{
        ADD("+"), SUB("-"), MUL("*"), DIV("/");
        private final String sign;
        Operation(String v) {
            this.sign = v;
        }
        public static Operation get(String s) {
            for(Operation o : Operation.values()) {
                if(o.sign.equals(s))
                    return o;
            }
            return null;
        }

        public static Long apply(Operation op, Long a, Long b) {
            return switch(op) {
                case ADD -> a + b;
                case SUB -> a - b;
                case MUL -> a * b;
                case DIV -> a / b;
            };
        }
    }

    record Monkey(String name, Long value, String a, Operation op, String b){
        public Monkey(String name, Long value) {
            this(name, value, null, null, null);
        }
    }

    private static Monkey parseString(String line) {
        String[] split = line.split(" ");
        String name = split[0].substring(0, 4);
        if(split.length == 2) {
            return new Monkey(name, Long.parseLong(split[1]));
        }
        return new Monkey(name, null, split[1].trim(), Operation.get(split[2].trim()), split[3].trim());
    }


    private Long solve(List<Monkey> monkeys, Map<String, Long> values) {
        int lastSize = 0;
        while(lastSize != values.size()) {
            lastSize = values.size();
            for (Monkey m : monkeys) {
                if (!values.containsKey(m.name)) {
                    if (values.containsKey(m.a) && values.containsKey(m.b)) {
                        values.put(m.name, Operation.apply(m.op, values.get(m.a), values.get(m.b)));
                    }
                }
            }
        }
        return values.get("root");
    }

    @Test
    public void testMonkey() {
        List<Monkey> monkeyList = FileReader.readFileObjectList("src/test/resources/2022/D21.txt", D21::parseString);

        Map<String, Long> values = monkeyList.stream()
                .filter(m->m.value != null).collect(Collectors.toMap(Monkey::name, Monkey::value));

        assertEquals(31017034894002L, solve(monkeyList, values));
    }

    // r = a + b ,  a = r - b,  b = r - a
    // r = a - b ,  a = r + b,  b = a - r
    // r = a * b ,  a = r / b,  b = r / a
    // r = a / b ,  a = r * b,  b = a / r

    private static Monkey isolate1(Monkey m) {
        return switch (m.op) {
            case ADD -> new Monkey(m.a, null, m.name, Operation.SUB, m.b);
            case SUB -> new Monkey(m.a, null, m.name, Operation.ADD, m.b);
            case MUL -> new Monkey(m.a, null, m.name, Operation.DIV, m.b);
            case DIV -> new Monkey(m.a, null, m.name, Operation.MUL, m.b);
        };
    }
    private static Monkey isolate2(Monkey m) {
        return switch (m.op) {
            case ADD -> new Monkey(m.b, null, m.name, Operation.SUB, m.a);
            case SUB -> new Monkey(m.b, null, m.a, Operation.SUB, m.name);
            case MUL -> new Monkey(m.b, null, m.name, Operation.DIV, m.a);
            case DIV -> new Monkey(m.b, null, m.a, Operation.DIV, m.name);
        };
    }

    private long calHuman(List<Monkey> monkeyList) {
        Map<String, Monkey> map = monkeyList.stream()
                .collect(Collectors.toMap(Monkey::name, Function.identity()));
        Map<String, Long> values = monkeyList.stream()
                .filter(m->m.value != null).collect(Collectors.toMap(Monkey::name, Monkey::value));
        values.remove("humn");

        // solve what we know
        solve(monkeyList, values);

        // Copy value to other side of root
        Monkey root = map.get("root");
        String start;
        if(values.get(root.a) == null) {
            start = root.a;
            values.put(root.a, values.get(root.b));
        } else {
            start = root.b;
            values.put(root.b, values.get(root.a));
        }

        // rewrite equations starting from unknown side of root
        while(true) {
            Monkey m = map.get(start);
            m = values.containsKey(m.a) ? isolate2(m) : isolate1(m);
            solve(List.of(m), values);
            if(m.name.equals("humn"))
                break;
            start = m.name;
        }
        return values.get("humn");
    }

    @Test
    public void testMonkey2() {
        assertEquals(301,
                calHuman(FileReader.readFileObjectList("src/test/resources/2022/D21_t.txt", D21::parseString)));

        assertEquals(3555057453229L,
                calHuman(FileReader.readFileObjectList("src/test/resources/2022/D21.txt", D21::parseString)));
    }

}
