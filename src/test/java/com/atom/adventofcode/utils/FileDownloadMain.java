package com.atom.adventofcode.utils;

import java.io.IOException;
import java.util.List;

public class FileDownloadMain {

    private static void downLoadDay(int year, int day) {
        try {
            FileDownloadService.getDay(year, day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downLoadYear(int year) {
        for(int i=0; i<26; i++) {
            downLoadDay(year, i);
        }
    }

    private static void downLoadYears(List<Integer> years) throws IOException {
        years.forEach(FileDownloadMain::downLoadYear);
    }

    public static void main(String[] args) throws IOException {
        downLoadDay(2015, 1);
    }
}
