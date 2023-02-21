package com.atom.adventofcode.y2014;

import com.atom.adventofcode.PartOne;
import com.atom.adventofcode.PartTwo;
import com.atom.adventofcode.common.FileReader;

import java.util.List;

/**
 * --- Day 2: I Was Told There Would Be No Math ---
 *
 * The elves are running low on wrapping paper, and so they need to submit an order for more. They have a list of the dimensions (length l, width w, and height h) of each present, and only want to order exactly as much as they need.
 *
 * Fortunately, every present is a box (a perfect right rectangular prism), which makes calculating the required wrapping paper for each gift a little easier: find the surface area of the box, which is 2*l*w + 2*w*h + 2*h*l. The elves also need a little extra paper for each present: the area of the smallest side.
 *
 * For example:
 *
 *     A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper plus 6 square feet of slack, for a total of 58 square feet.
 *     A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper plus 1 square foot of slack, for a total of 43 square feet.
 *
 * All numbers in the elves' list are in feet. How many total square feet of wrapping paper should they order?
 *
 * Your puzzle answer was 1588178.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 * --- Part Two ---
 *
 * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to worry about the length they need to order, which they would again like to be exact.
 *
 * The ribbon required to wrap a present is the shortest distance around its sides, or the smallest perimeter of any one face. Each present also requires a bow made out of ribbon as well; the feet of ribbon required for the perfect bow is equal to the cubic feet of volume of the present. Don't ask how they tie the bow, though; they'll never tell.
 *
 * For example:
 *
 *     A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
 *     A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present plus 1*1*10 = 10 feet of ribbon for the bow, for a total of 14 feet.
 *
 * How many total feet of ribbon should they order?
 */
public class D2 implements PartOne<String, Integer>, PartTwo<String, Integer> {
    public record Parcel(int l, int w, int h){};

    @Override
    public Integer partOne(String inp) {
//                        readParcels("src/test/resources/2015/D2.txt")
//                        .stream().mapToInt(this::calculatePaper).sum());

        return null;
    }

    @Override
    public Integer partTwo(String inp) {
        return null;
    }



    public static Parcel getParcel(String inp) {
        String[] spit = inp.split("x");
        return new Parcel(Integer.parseInt(spit[0]),Integer.parseInt(spit[1]),Integer.parseInt(spit[2]));
    }

    public static int calculatePaper(Parcel p) {
        int s1 = p.l*p.w;
        int extra = s1;
        int s2 = p.w*p.h;
        extra = Math.min(extra, s2);
        int s3 = p.h*p.l;
        extra = Math.min(extra, s3);

        return (2*(s1+s2+s3))+extra;
    }

    public static int calculateRibbon(Parcel p) {
        int len = 2*(p.h+p.l);
        len = Math.min(len, 2*(p.w+p.l));
        len = Math.min(len, 2*(p.w+p.h));

        // Bow
        len += p.h*p.l*p.w;
        return len;
    }
}
