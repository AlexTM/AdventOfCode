package com.atom.adventofcode.y2019.orbits;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    final private List<Node> nodes;
    final private String name;

    public Node(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodes=" + nodes +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public String getName() {
        return name;
    }

}
