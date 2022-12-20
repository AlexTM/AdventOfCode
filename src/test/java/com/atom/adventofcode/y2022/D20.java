package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D20 {

    static class Node {
        Long value;
        Node next, prev;
        public Node(long value, Node prev) {
            this.value = value;
            this.prev = prev;
        }
    }

    private Node createdLinkedList(List<Long> copy) {
        Node prev = null, first = null;
        for(Long i : copy) {
            Node n = new Node(i, prev);
            if(prev != null)
                prev.next = n;
            prev = n;
            if(first == null)
                first = n;
        }
        first.prev = prev;
        prev.next = first;
        return first;
    }

    private Node mix(final List<Long> nums, int loops) {
        Node first = createdLinkedList(nums);
        List<Node> initialLocation = new ArrayList<>();

        Node current = first;
        for(int i=0; i<nums.size(); i++) {
            initialLocation.add(current);
            current = current.next;
        }

        for(int loop=0; loop < loops; loop++) {
            for (Node n : initialLocation) {
                long remainder = n.value % (nums.size() - 1);

                if (first == n)
                    first = first.next;

                Node p = n;
                if (remainder > 0) {
                    for (int i = 0; i < remainder + 1; i++)
                        p = p.next;
                    swapOut(n, p);
                } else if (remainder < 0) {
                    for (int i = 0; i < Math.abs(remainder); i++)
                        p = p.prev;
                    swapOut(n, p);
                }
            }
        }

        return first;
    }

    private void swapOut(Node from, Node to) {
        from.prev.next = from.next;
        from.next.prev = from.prev;

        from.prev = to.prev;
        from.next = to;
        to.prev.next = from;
        to.prev = from;
    }

    private long groveCoords(Node node) {
        while(node.value != 0){ node = node.next; }

        long sum = 0;
        long c=0;
        while(c != 1000){ node = node.next; c++; }
        sum += node.value;
        while(c != 2000){ node = node.next; c++; }
        sum += node.value;
        while(c != 3000){ node = node.next; c++; }
        sum += node.value;
        return sum;
    }

    @Test
    public void testEnc() {
        List<Long> nums = Arrays.stream(FileReader.readFileIntegerArray("src/test/resources/2022/D20_t.txt"))
                .map(i -> (long) i).toList();
        assertEquals(3, groveCoords(mix(nums, 1)));

        nums = Arrays.stream(FileReader.readFileIntegerArray("src/test/resources/2022/D20.txt"))
                .map(i -> (long) i).toList();
        assertEquals(11037, groveCoords(mix(nums, 1)));
    }

    @Test
    public void testEnc2() {
        List<Long> nums = Arrays.stream(FileReader.readFileIntegerArray("src/test/resources/2022/D20_t.txt"))
                .map(i -> (long) i).toList();

        List<Long> biggerNums = nums.stream().map(l -> l * 811589153L).toList();
        assertEquals(1623178306, groveCoords(mix(biggerNums, 10)));


        nums = Arrays.stream(FileReader.readFileIntegerArray("src/test/resources/2022/D20.txt"))
                .map(i -> (long) i).toList();

        biggerNums = nums.stream().map(l -> l * 811589153L).toList();
        assertEquals(3033720253914L, groveCoords(mix(biggerNums, 10)));
    }
}
