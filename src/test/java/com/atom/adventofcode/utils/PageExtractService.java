package com.atom.adventofcode.utils;

import com.atom.adventofcode.utils.html.Article;
import com.atom.adventofcode.utils.html.Page;
import io.github.furstenheim.CopyDown;
import io.micrometer.common.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class PageExtractService {
    private static final String PACKAGE = "com.atom.adventofcode";
    private static final String FILE_TEMPLATE = "src/main/resources/template.txt";

    public static Page testPageExtract(int year, int day, String content) {

        Document doc = Jsoup.parse(content);

        Elements main = doc.select("main");
        Elements article = main.select("article");

        // Search for answers
        Elements answers = main.select("p:matches(^Your puzzle answer was.*)");
        String answer1 = null, answer2 = null;
        if(answers.size() > 0) {
            answer1 = answers.get(0).child(0).text();
        }
        if(answers.size() > 1) {
            answer2 = answers.get(1).child(0).text();
        }


        Article article1 = null, article2 = null;
        for (Element a : article) {
            String title = "";
            StringBuilder sb = new StringBuilder();
            for (Element e : a.children()) {
                if (e.normalName().equals("h2")) {
                    title = e.toString();
                } else {
                    sb.append(e.toString());
                }
            }
            if (article1 == null)
                article1 = new Article(title, sb.toString(), answer1);
            else
                article2 = new Article(title, sb.toString(), answer2);
        }

        return new Page(year, day, article1, article2);
    }

    private static String convertToMarkDown (String html, boolean addJavaComment) {
        CopyDown converter = new CopyDown();
        String markdown = converter.convert(html);

        // limit line length
        StringBuilder sb = new StringBuilder();
        for(String line : markdown.split("\n")) {
            int count = 0;
            for(char c : line.toCharArray()) {
                if(count > 110 && c == ' '){
                    count = 0;
                    sb.append("\n");
                } else
                    sb.append(c);
                count++;
            }
            sb.append("\n");
        }

        String res = sb.toString();

        if(addJavaComment) {
            StringBuilder sb2 = new StringBuilder();
            for(String line : res.split("\n")) {
                sb2.append(line);
                sb2.append("\n     * ");
            }
            res = sb2.toString();
        }

        return res;
    }

    public static void generatePage(Page page) throws IOException {

        String className = "Day"+page.day();
        String packageName = PACKAGE+".test"+page.year();
        String fileName = "src/main/java/com/atom/adventofcode/test"+page.year()+"/"+className+".java";

        Path filePath = Path.of(fileName);
        if(!filePath.toFile().exists()) {
            filePath = Path.of(FILE_TEMPLATE);
        }
        String fileContent = Files.readString(filePath);

        // TODO proper template engine?
        fileContent = fileContent.replace("{{CLASS_PACKAGE}}", packageName);
        fileContent = fileContent.replace("{{CLASS_DOC}}", page.partOne().title());
        fileContent = fileContent.replace("{{CLASS_NAME}}", className);
        fileContent = fileContent.replace("{{PART_ONE}}",
                convertToMarkDown(page.partOne().content(), true));

        if(!StringUtils.isBlank(page.partOne().answer()))
            fileContent = fileContent.replace("{{PART_ONE_ANSWER}}", page.partOne().answer());

        if(!StringUtils.isBlank(page.partTwo().answer()))
            fileContent = fileContent.replace("{{PART_TWO_ANSWER}}", page.partTwo().answer());

        if(!StringUtils.isBlank(page.partTwo().content())) {
            fileContent = fileContent.replace("{{PART_TWO}}",
                    convertToMarkDown(page.partTwo().content(), true));
        }

        try(PrintWriter printWriter = new PrintWriter(new FileWriter(fileName))) {
            printWriter.print(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
