package com.atom.adventofcode;

public record Challenge<T, U>(String testData, String data, T partOne, U partTwo) {}
