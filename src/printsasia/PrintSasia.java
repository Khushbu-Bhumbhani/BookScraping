/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printsasia;

import connectionManager.Utility;
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
public class PrintSasia {

    public static void main(String[] args) {
        String categoryURL = "http://printsasia.co.uk/books/art";
        startCategoryScrape(categoryURL);
    }

    private static void startCategoryScrape(String categoryURL) {
        int pageNo = 0;
        String category = StringUtils.substringAfterLast(categoryURL, "/");
        // int totalPages=
        try {
            Document doc = Jsoup.connect(categoryURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            for (Element e : doc.getElementsByClass("book")) {
                String link = e.getElementsByTag("a").first().attr("href");
                link = link.replace("..", "");
                link = "http://www.printsasia.co.uk" + link;
                //System.out.println("" + link);
                detailScrape(link, category);
                Thread.sleep(1000);
            }
        } catch (IOException ex) {
            Logger.getLogger(PrintSasia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrintSasia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void detailScrape(String link, String category) {
        try {
            Document doc = Jsoup.connect(link)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();

            String title = "";
            String author = "";
            String imageURL = "";
            String publicationDate = "";
            String binding = "";
            String originalPrice = "";
            String weight = "";
            String listPrice = "";
            String noOfPages = "";
            String ISBN10 = "";
            String ISBN13 = "";
            String publisher = "";
            String desc = "";

            title = doc.getElementsByClass("bd-title").first().text();
            if (!doc.getElementsByAttributeValue("itemprop", "author").isEmpty()) {
                author = doc.getElementsByAttributeValue("itemprop", "author").first().text();
            }
            if (!doc.getElementsByAttributeValue("itemprop", "publisher").isEmpty()) {
                publisher = doc.getElementsByAttributeValue("itemprop", "publisher").first().text();
            }
            if (doc.getElementById("iBook") != null) {
                imageURL = doc.getElementById("iBook").attr("src");
            }
            if (doc.getElementById("ltPrice") != null) {
                listPrice = doc.getElementById("ltPrice").text();
            }
            if (!doc.getElementsByClass("bd-binding").isEmpty()) {
                binding = doc.getElementsByClass("bd-binding").first().text();
            }
            if (!doc.getElementsByClass("bd-price-row").isEmpty()) {
                originalPrice = doc.getElementsByClass("bd-price-row").first().text();
            }
            Element table = doc.getElementsByClass("bd-properties").first();
            publicationDate = StringUtils.substringBetween(table.html(), "Published In:", "</tr");
            ISBN10 = StringUtils.substringBetween(table.html(), "ISBN-10:", "</tr");
            ISBN13 = StringUtils.substringBetween(table.html(), "ISBN-13:", "</tr");
            weight = StringUtils.substringBetween(table.html(), "Weight:", "</tr");
            noOfPages = StringUtils.substringBetween(table.html(), "Pages:", "</tr");
            publicationDate=Utility.html2text(publicationDate);
            ISBN10=Utility.html2text(ISBN10);
            ISBN13=Utility.html2text(ISBN13);
            weight=Utility.html2text(weight);
            noOfPages=Utility.html2text(noOfPages);
            if (table.nextElementSibling() != null && table.nextElementSibling().tagName().equalsIgnoreCase("p")) {
                desc = table.nextElementSibling().text();
            }
            System.out.println("\"" + title + "\";\"" + author + "\";" + ISBN10 + ";" + ISBN13 + ";\"" + publisher + "\";" + publicationDate + ";" + binding
                    + ";" + originalPrice + ";" + listPrice + ";" + noOfPages + ";" + weight + ";" + imageURL + ";\"" + desc + "\";" + link);
        } catch (IOException ex) {
            Logger.getLogger(PrintSasia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
