package com.atom.adventofcode.y2019;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Fuel required to launch a given module is based on its mass. Specifically, to find the fuel required for a module,
 * take its mass, divide by three, round down, and subtract 2.
 *
 * For example:
 *
 * For a mass of 12, divide by 3 and round down to get 4, then subtract 2 to get 2.
 * For a mass of 14, dividing by 3 and rounding down still yields 4, so the fuel required is also 2.
 * For a mass of 1969, the fuel required is 654.
 * For a mass of 100756, the fuel required is 33583.
 * The Fuel Counter-Upper needs to know the total fuel requirement. To find it, individually calculate the fuel needed
 * for the mass of each module (your puzzle input), then add together all the fuel values.
 *
 * What is the sum of the fuel requirements for all of the modules on your spacecraft?
 */
public class Day1P1 {


    @Test
    public void testFuel() {
        assertEquals(2, FuelCalculator.calculateFuel(12));
        assertEquals(2, FuelCalculator.calculateFuel(14));
        assertEquals(654, FuelCalculator.calculateFuel(1969));
        assertEquals(33583, FuelCalculator.calculateFuel(100756));

    }

    @Test
    public void testTotalFuel() throws IOException, URISyntaxException {
       totalFuel("module_mass");
    }

    public int totalFuel(String fname) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader()
                .getResource(fname).toURI());

        Stream<String> lines = Files.lines(path);
        int totalFuel = lines.map(Integer::valueOf).mapToInt(FuelCalculator::calculateFuel).sum();
        lines.close();

        System.out.println(totalFuel);
        return totalFuel;
    }

}
