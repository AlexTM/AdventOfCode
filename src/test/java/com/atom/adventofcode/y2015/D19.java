package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D19 {

    record Rule(String start, String end){};

    private static Rule readRules(String inp) {
        String[] split = inp.split("=>");
        return new Rule(split[0].trim(), split[1].trim());
    }

    private Set<String> generateFromRule(Rule rule, String input) {
        Set<String> molecules = new HashSet<>();

        int index = -1;
        while((index = input.indexOf(rule.start, index + 1)) >= 0) {
            String stringBuffer = input.substring(0, index) +
                    rule.end +
                    input.substring(index + rule.start.length());
            molecules.add(stringBuffer);
        }

        return molecules;
    }

    private Set<String> iterate(List<Rule> ruleList, String input) {
        Set<String> molecules = new HashSet<>();
        for(Rule rule : ruleList) {
            molecules.addAll(generateFromRule(rule, input));
        }
        return molecules;
    }

    @Test
    public void testMolecule() {

        String inp = "" +
                "H => HO\n" +
                "H => OH\n" +
                "O => HH";

        List<Rule> ruleList = FileReader.readObjectList(inp, D19::readRules);
        assertEquals(4, iterate(ruleList, "HOH").size());
        assertEquals(7, iterate(ruleList, "HOHOHO").size());
    }

    @Test
    public void testMolecule2() {
        String inp = "Al => ThF\n" +
                "Al => ThRnFAr\n" +
                "B => BCa\n" +
                "B => TiB\n" +
                "B => TiRnFAr\n" +
                "Ca => CaCa\n" +
                "Ca => PB\n" +
                "Ca => PRnFAr\n" +
                "Ca => SiRnFYFAr\n" +
                "Ca => SiRnMgAr\n" +
                "Ca => SiTh\n" +
                "F => CaF\n" +
                "F => PMg\n" +
                "F => SiAl\n" +
                "H => CRnAlAr\n" +
                "H => CRnFYFYFAr\n" +
                "H => CRnFYMgAr\n" +
                "H => CRnMgYFAr\n" +
                "H => HCa\n" +
                "H => NRnFYFAr\n" +
                "H => NRnMgAr\n" +
                "H => NTh\n" +
                "H => OB\n" +
                "H => ORnFAr\n" +
                "Mg => BF\n" +
                "Mg => TiMg\n" +
                "N => CRnFAr\n" +
                "N => HSi\n" +
                "O => CRnFYFAr\n" +
                "O => CRnMgAr\n" +
                "O => HP\n" +
                "O => NRnFAr\n" +
                "O => OTi\n" +
                "P => CaP\n" +
                "P => PTi\n" +
                "P => SiRnFAr\n" +
                "Si => CaSi\n" +
                "Th => ThCa\n" +
                "Ti => BP\n" +
                "Ti => TiTi\n" +
                "e => HF\n" +
                "e => NAl\n" +
                "e => OMg";
        List<Rule> ruleList = FileReader.readObjectList(inp, D19::readRules);
        assertEquals(509, iterate(ruleList, "CRnCaSiRnBSiRnFArTiBPTiTiBFArPBCaSiThSiRnTiBPBPMgArCaSiRnTiMgArCaSiThCaSiRnFArRnSiRnFArTiTiBFArCaCaSiRnSiThCaCaSiRnMgArFYSiRnFYCaFArSiThCaSiThPBPTiMgArCaPRnSiAlArPBCaCaSiRnFYSiThCaRnFArArCaCaSiRnPBSiRnFArMgYCaCaCaCaSiThCaCaSiAlArCaCaSiRnPBSiAlArBCaCaCaCaSiThCaPBSiThPBPBCaSiRnFYFArSiThCaSiRnFArBCaCaSiRnFYFArSiThCaPBSiThCaSiRnPMgArRnFArPTiBCaPRnFArCaCaCaCaSiRnCaCaSiRnFYFArFArBCaSiThFArThSiThSiRnTiRnPMgArFArCaSiThCaPBCaSiRnBFArCaCaPRnCaCaPMgArSiRnFYFArCaSiThRnPBPMgAr").size());
    }

    private int fabrication(List<Rule> ruleList, String current, String target, int index) {
        int steps = 0;

        while(!current.equals(target)) {

//        int index = findFirstDivergence(current, target);
            Set<String> candidates = iterate(ruleList, current);
//        String best = "";
            int bestSize = 0;
            for (String c : candidates) {
                index = findFirstDivergence(c, target);
                if (bestSize < index) {
                    bestSize = index;
                    current = c;
                }
            }
            steps++;
        }
        return steps;
    }

    private int findFirstDivergence(String current, String target) {
        for(int i=0; i<current.length() && i <target.length(); i++) {
            if(current.charAt(i) != target.charAt(i))
                return i;
        }
        return current.length();
    }

    private int fabrication2(List<Rule> ruleList, String target) {
        // reverse string

        int steps = 0;
        while(!target.equalsIgnoreCase("e")) {


            steps++;
        }

        return steps;
    }

    @Test
    public void testFabrication() {
        String inp = "e => H\n" +
                "e => O\n" +
                "H => HO\n" +
                "H => OH\n" +
                "O => HH";
        List<Rule> ruleList = FileReader.readObjectList(inp, D19::readRules);
        assertEquals(4, fabrication(ruleList, "e", "HOH", 0));
    }
}
