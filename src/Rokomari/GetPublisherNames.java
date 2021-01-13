/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class GetPublisherNames {

    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
        String url = "https://www.rokomari.com/book/publishers?ref=mm";
        boolean hasNextPage = true;

        int pageno = 2;

        do {
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                // System.out.println(""+doc.text());
                Element ul = doc.getElementsByClass("authorList").first();
                for (Element li : ul.getElementsByTag("li")) {
                    //  System.out.println("" + li.getElementsByTag("img").first().attr("alt")+";"+li.getElementsByTag("h2").first().text());
                    String content = li.getElementsByTag("img").first().attr("alt") + ";" + li.getElementsByTag("h2").first().text() + LINE_SEPARATOR;
                    Files.write(Paths.get("E:\\RokomariPublisher.txt"), content.getBytes(), StandardOpenOption.APPEND);
                }
                if (pageno <= 137) {
                    url = "https://www.rokomari.com/book/publishers?ref=mm&page=" + pageno;
                    pageno++;
                    System.out.println("Page:" + pageno);
                } else {
                    hasNextPage = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(GetPublisherNames.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);

    }
}
