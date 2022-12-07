package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D7 {

    record Node(Node parent, String name, long size, List<Node> children) {
        public Node(Node parent, String name) {
            this(parent, name, 0, new ArrayList<>());
        }
        public Node(Node parent, String name, long size) {
            this(parent, name, size, null);
        }
    }

    private Node buildTree(List<String> stringList) {
        final Node rootNode = new Node(null, "/");
        Node currentNode = rootNode;

        for(String s : stringList) {
            String[] split = s.split(" ");
            if(s.charAt(0) == '$') {
                if ("cd".equalsIgnoreCase(split[1].trim())) {
                    if (split[2].trim().equalsIgnoreCase("/"))
                        currentNode = rootNode;
                    else if (split[2].trim().equalsIgnoreCase(".."))
                        currentNode = currentNode.parent;
                    else {
                        for (Node n : currentNode.children) {
                            if (n.name.equalsIgnoreCase(split[2].trim())) {
                                currentNode = n;
                                break;
                            }
                        }
                    }
                }
            } else {
                if(split[0].trim().equalsIgnoreCase("dir")) {
                    currentNode.children.add(new Node(currentNode, split[1]));
                } else {
                    currentNode.children.add(new Node(currentNode, split[1], Long.parseLong(split[0])));
                }
            }
        }
        return rootNode;
    }

    private long getDirSize(Node node, List<Long> dirSizes) {
        if(node.children == null)
            return node.size;

        long sum = 0;
        for(Node c : node.children)
            sum += getDirSize(c, dirSizes);

        dirSizes.add(sum);
        return sum;
    }

    private long findDirToDelete(List<Long> dirs, long fs, long up) {
        Collections.sort(dirs);
        long toFind = up - fs + dirs.get(dirs.size()-1);
        return dirs.stream().filter(i -> i > toFind).findFirst().orElseThrow();
    }

    @Test
    public void testTree() {
        Node rootNode = buildTree(
                FileReader.readFileObjectList("src/test/resources/2022/D7_t.txt", s->s));

        List<Long> dirSizes = new ArrayList<>();
        getDirSize(rootNode, dirSizes);

        assertEquals(95437,
                dirSizes.stream().filter(i -> i <= 100000)
                        .reduce(0L, Long::sum));
        assertEquals(24933642, findDirToDelete(dirSizes, 70000000L, 30000000L));

        rootNode = buildTree(
                FileReader.readFileObjectList("src/test/resources/2022/D7.txt", s->s));

        dirSizes = new ArrayList<>();
        getDirSize(rootNode, dirSizes);

        assertEquals(919137,
                dirSizes.stream().filter(i -> i <= 100_000)
                .reduce(0L, Long::sum));

        assertEquals(2877389, findDirToDelete(dirSizes, 70000000L, 30000000L));
    }
}

