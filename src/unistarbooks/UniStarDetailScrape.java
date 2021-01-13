/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unistarbooks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class UniStarDetailScrape {

    public static void main(String[] args) {
        String link = "http://www.unistarbooks.com/play/670-main-taan-ik-sarangi-haan.html";
        startDetailScrape(link);
    }

    private static void startDetailScrape(String link) {
        try {
            Document doc = Jsoup.connect(link)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();

            String title = "";
            //String cat = "";
            String auth = "";
            String imageURL = "";
            String ISBN = "";
            String year = "";
            String edition = "";
            String binding = "";
            String price = "";
            String desc = "";
            //  String subject = "";
            String language = "";
            // String publication = "";
            Element div = doc.getElementById("center_column");
            title = doc.getElementById("pb-left-column").text();
            year=StringUtils.substringBetween(div.html(), "Year of Publishing:", "</div>");
            auth=StringUtils.substringBetween(div.html(), "Year of Publishing:", "</div>");
            binding=StringUtils.substringBetween(div.html(), "Year of Publishing:", "</div>");
            language=StringUtils.substringBetween(div.html(), "Year of Publishing:", "</div>");

        } catch (IOException ex) {
            Logger.getLogger(UniStarDetailScrape.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
