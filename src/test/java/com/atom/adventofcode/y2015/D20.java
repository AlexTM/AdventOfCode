package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D20 {

    private int findHouseNumber(int targetPresents) {
        int houseNumber = 1;
        while (true) {
            if (presentsDeliveredForHouse(houseNumber) >= targetPresents)
                return houseNumber;
            houseNumber++;
            if (houseNumber % 1000 == 0) {
                System.out.println("Total :house " + houseNumber + " = " + presentsDeliveredForHouse(houseNumber));
            }
        }
    }


    private int presentsDeliveredForHouse(int houseNumber) {
        int presents = 0;
        for(int i=1; i<=houseNumber; i++) {
            if(houseNumber%i == 0) {
                presents += i*10;
            }
        }
        return presents;
    }

    @Test
    public void testPresentDelivery() {
        assertEquals(10, presentsDeliveredForHouse(1));
        assertEquals(30, presentsDeliveredForHouse(2));
        assertEquals(40, presentsDeliveredForHouse(3));
        assertEquals(70, presentsDeliveredForHouse(4));


//        assertEquals(70, presentsDeliveredForHouse(2300000));
//        assertEquals(70, findHouseNumber3(34000000));

        assertEquals(70, presentsDeliveredForHouse(200001));

        // 3931200
        // 34_000_000
        // 59058720
        // 5810640
    }
}
