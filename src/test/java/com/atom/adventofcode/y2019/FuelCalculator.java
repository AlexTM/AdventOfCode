package com.atom.adventofcode.y2019;

public class FuelCalculator {

    public static int calculateFuelRepeat(int mass) {
        int totalFuel = 0;
        do {
            mass = calculateFuel(mass);
            totalFuel += mass;
        } while(mass > 0);
        return totalFuel;
    }

    public static int calculateFuel(int mass) {
        double fuel = (double)mass / 3.0;
        int intFuel = (int)Math.floor(fuel) - 2;
        if(intFuel < 0) return 0;
        return intFuel;
    }

}
