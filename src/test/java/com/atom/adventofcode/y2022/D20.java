package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D20 {

    class Node {
        Integer value;
        Node next, prev;
        public Node(int value, Node prev) {
            this.value = value;
            this.prev = prev;
        }
    }

    private Node createdLinkedList(List<Integer> copy) {
        Node prev = null, first = null;
        for(Integer i : copy) {
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

    private List<Integer> createList(Node first, int size) {
        List<Integer> res = new ArrayList<>();
        Node current = first;
        for(int i=0; i<size; i++) {
            res.add(current.value);
            current = current.next;
        }
        return res;
    }

    private Node mixing(final List<Integer> nums) {
        Node first = createdLinkedList(nums);
        List<Node> initialLocation = new ArrayList<>();

        Node current = first;
        for(int i=0; i<nums.size(); i++) {
            initialLocation.add(current);
            current = current.next;
        }

        int listSize = nums.size();

        for(Node n : initialLocation) {

            int remainder = n.value % listSize;
//            int remainder = n.value;
            Node p = n;
            if(first == p) {
                first = first.next;
            }
            if(remainder > 0) {
                for(int i=0; i<remainder+1; i++) {
                    p = p.next;
                }
                swapOut(n, p);
            } else if(remainder < 0){
                for(int i=0; i<Math.abs(remainder); i++) {
                    p = p.prev;
                }
                swapOut(n, p);
            }
        }

        return first;
    }

    private void swapOut(Node from, Node to) {

        // cut out from
        from.prev.next = from.next;
        from.next.prev = from.prev;

        from.prev = to.prev;
        from.next = to;
        to.prev.next = from;
        to.prev = from;
    }

    private int groveCoords(Node node) {
        // find 0
        while(node.value != 0){ node = node.next; }

        int sum = 0;
        int c=0;
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
//        Node node = mixing(
//                Arrays.asList(FileReader.readFileIntegerArray("src/test/resources/2022/D20_t.txt")));
//        assertEquals(3, groveCoords(node));

        // -11286
        Node node = mixing(
                Arrays.asList(FileReader.readFileIntegerArray("src/test/resources/2022/D20.txt")));
        assertEquals(0, groveCoords(node));

    }

}
