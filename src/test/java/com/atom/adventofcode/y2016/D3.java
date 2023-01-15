package com.atom.adventofcode.y2016;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D3 {

    record Triangle(int a, int b, int c){};
    private static Triangle parseString(String s) {
        return new Triangle(
                Integer.parseInt(s.substring(2, 5).trim()),
                Integer.parseInt(s.substring(7, 10).trim()),
                Integer.parseInt(s.substring(12, 15).trim()));
    }

    private static boolean isValid(Triangle tri) {
        if(tri.a + tri.b <= tri.c)
            return false;
        if(tri.a + tri.c <= tri.b)
            return false;
        if(tri.c + tri.b <= tri.a)
            return false;
        return true;
    }

    private List<Triangle> jumbleTriangle(List<Triangle> triangles) {
        List<Triangle> newTriangleList = new ArrayList<>();
        for(int i=0; i<triangles.size()/3; i++) {
            Triangle a = triangles.get(i*3);
            Triangle b = triangles.get(i*3+1);
            Triangle c = triangles.get(i*3+2);
            newTriangleList.add(new Triangle(a.a, b.a, c.a));
            newTriangleList.add(new Triangle(a.b, b.b, c.b));
            newTriangleList.add(new Triangle(a.c, b.c, c.c));
        }
        return newTriangleList;
    }

    @Test
    public void testTriangle() {
        List<Triangle> triangleList = FileReader.readFileObjectList("src/test/resources/2016/D3.txt", D3::parseString);
        assertEquals(862, triangleList.stream().filter(D3::isValid).count());
        assertEquals(1577, jumbleTriangle(triangleList).stream().filter(D3::isValid).count());
    }
}
