package com.atom.adventofcode.services;

import com.atom.adventofcode.AdventOfCode;
import com.atom.adventofcode.Challenge;
import com.atom.adventofcode.PartOne;
import com.atom.adventofcode.PartTwo;
import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {
    private static final Logger logger = LoggerFactory.getLogger(ProblemService.class);

    @Autowired
    private List<PartOne<?,?>> partOneList;

    @Autowired
    private List<PartTwo<?,?>> partTwoList;

    @Autowired
    private ObjectMapper objectMapper;

    private List<String> orderedPackageList;

    @PostConstruct
    public void init() {
        orderedPackageList =
                partOneList.stream().map(po -> po.getClass().getName())
                        .sorted().collect(Collectors.toCollection(ArrayList::new));

        for(String s : orderedPackageList) {
            logger.info(s);
        }
//        for(PartOne<?, ?> po : partOneList) {
//            logger.info("{}, {}",
//                    po.getClass().getPackageName(),
//                    po.getClass().getSimpleName());
//        }


    }

        //    @PostConstruct
    public void initB() {

        // TODO load data for test
        for(PartOne po : partOneList) {
            logger.info("Running {}", 1);
//            String inp = FileReader.readFileForObject("src/main/resources/2014/D1.txt", "", (s, a)->s);
            String inp =
                    FileReader.readFileForObject("src/main/resources/2014/D1.json",
                            new StringBuilder(), (s, a)->a.append(s)).toString();

            try {
                var r = objectMapper.readValue(inp, Challenge.class);
                var result = po.partOne(r.data());

                if(!r.partOne().equals(result)) {
                    logger.error("Error {}", result);
                }

                logger.info("Result {}", result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        for(PartTwo po : partTwoList) {
            logger.info("Running {}", 1);
//            String inp = FileReader.readFileForObject("src/main/resources/2014/D1.txt", "", (s, a)->s);
            String inp =
                    FileReader.readFileForObject("src/main/resources/2014/D1.json",
                            new StringBuilder(), (s, a)->a.append(s)).toString();

            try {
                var r = objectMapper.readValue(inp, Challenge.class);
                var result = po.partTwo(r.data());

                if(!r.partTwo().equals(result)) {
                    logger.error("Error {}", result);
                }


                logger.info("Result {}", result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public List<String> getProblems() {
        return orderedPackageList;
    }
}
