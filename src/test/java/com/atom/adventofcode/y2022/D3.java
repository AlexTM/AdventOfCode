package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D3 {

    private static Integer convert(int c) {
        if(c >= 'a' && c <= 'z')
            c = c - 'a' + 1;
        else if(c >= 'A' && c <= 'Z')
            c = c - 'A' + 27;
        return c;
    }

    private int sumAllRucksack(List<String> stringList) {
        return stringList.stream().map(D3::findCommon).reduce(0, Integer::sum);
    }

    private static int findCommon(String data) {
        String comp1 = data.substring(0, data.length()/2);
        String comp2 = data.substring(data.length()/2);

        Set<Integer> setComp1 =
                comp1.chars().mapToObj(D3::convert).collect(Collectors.toCollection(HashSet::new));
        Set<Integer> setComp2 =
                comp2.chars().mapToObj(D3::convert).collect(Collectors.toCollection(HashSet::new));

        setComp1.retainAll(setComp2);

        return setComp1.stream().reduce(0, Integer::sum);
    }

    @Test
    public void testPacking() {
        assertEquals(8185, sumAllRucksack(
                FileReader.readFileStringList("src/test/resources/2022/D3.txt")));
    }

    private int findBadge(List<String> inputs) {
        Set<Integer> common = new HashSet<>();
        int sum = 0;
        for(int i=0; i<inputs.size(); i++) {
            Set<Integer> integerSet =
                    inputs.get(i).chars().mapToObj(D3::convert)
                            .collect(Collectors.toCollection(HashSet::new));
            common.retainAll(integerSet);
            if(i%3 == 2) {
                sum += common.stream().findAny().get();
            } else if(i%3 == 0) {
                common = integerSet;
            }

        }
        return sum;
    }

    @Test
    public void testFindBadge() {
        assertEquals(2817, findBadge(
                FileReader.readFileStringList("src/test/resources/2022/D3.txt")));
    }

}
