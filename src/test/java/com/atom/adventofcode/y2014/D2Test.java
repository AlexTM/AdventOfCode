package com.atom.adventofcode.y2014;

import com.atom.adventofcode.AocTest;
import com.atom.adventofcode.Challenge;
import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D2Test extends AocTest {

    public static List<D2.Parcel> readParcels(String content) {
        return FileReader.readObjectList(content, D2::getParcel);
    }


    @Test
    public void testWrappingPaper() throws JsonProcessingException {

        Challenge<?, ?> challenge = getChallenge();
        System.out.println(challenge);

//        List<D2.Parcel> f = D2::readParcels(challenge.data());

//        assertEquals(challenge.partOne(), f.stream().mapToInt(D2::calculatePaper).sum());
    }

    @Test
    public void testRibbon() {
//        assertEquals(34, D2::partTwo(getParcel("2x3x4")));
//        assertEquals(3783758,
//                readParcels("src/test/resources/2015/D2.txt")
//                        .stream().mapToInt(this::calculateRibbon).sum());
    }
}
