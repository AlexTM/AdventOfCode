package com.atom.adventofcode.services;

import com.atom.adventofcode.PartOne;
import com.atom.adventofcode.PartTwo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DummyProblem implements PartOne<String, Integer>, PartTwo<String, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(DummyProblem.class);

    @Override
    public Integer partOne(String inp) {
        logger.info("Starting partOne");
        logger.info("Finishing partOne");
        return null;
    }

    @Override
    public Integer partTwo(String inp) {
        logger.info("Starting partTwo");
        logger.info("Finishing partTwo");
        return null;
    }
}
