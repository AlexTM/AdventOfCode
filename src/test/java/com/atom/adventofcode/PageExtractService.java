package com.atom.adventofcode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class PageExtractService {

    @Test
    public void testPageExtract() {
        Document doc = Jsoup.parse("src/test/resources/1.html");
        System.out.println(doc);
        Elements main = doc.select("main");
        System.out.println(main);
    }

}
