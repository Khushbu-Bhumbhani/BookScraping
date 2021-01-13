/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jainbookagecy;

import IBPbooks.IBPBooksLinkScraping;
import connectionManager.MyConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class JainBookLinkScraping {

    static int website_id = 2;

    public static void main(String[] args) {
        getCategory();
    }

    private static void getCategory() {
        String url = "https://www.jainbookagency.com/subcategory.aspx";
        try {
            System.out.println("" + url);
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            Element catLink = doc.getElementsByClass("cat-link").first();
            for (Element a : catLink.getElementsByTag("a")) {
                String catUrl = "https://www.jainbookagency.com" + a.attr("href");
                System.out.println("" + catUrl);
                linkScraping(catUrl, a.text());
            }
            catLink = doc.getElementsByClass("cat-link").get(1);
            for (Element a : catLink.getElementsByTag("a")) {
                String catUrl = "https://www.jainbookagency.com" + a.attr("href");
                System.out.println("" + catUrl);
                linkScraping(catUrl, a.text());

            }
        } catch (IOException ex) {
            Logger.getLogger(JainBookLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void linkScraping(String catUrl, String category) {
        boolean hasNextPage = false;
        do {
            System.out.println("Getting..." + catUrl);
            System.out.println("" + category);
            try {
                Document doc = Jsoup.connect(catUrl)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                String insertQuery = "INSERT INTO `books`.`book_master`\n"
                        + "(\n"
                        + "`book_url`,website_source_id,page_url,category_name)\n"
                        + "VALUES\n";
                Element div = doc.getElementById("ContentPlaceHolder1_products");
                if (div != null) {
                    for (Element item : div.getElementsByClass("item-detail")) {
                        String detailURL = "https://www.jainbookagency.com" + item.getElementsByTag("a").first().attr("href");
                   //     System.out.println("" + detailURL);
                        insertQuery = insertQuery + " ('" + detailURL + "',"
                                + website_id + ",'" + catUrl + "','" + category + "'),";
                    }
                } else {
                    System.out.println("---> NO item found " + catUrl);
                }
                MyConnection.getConnection("books");
                MyConnection.insertData(insertQuery.substring(0, insertQuery.length() - 1));
                //pagination
                if (!doc.getElementsByClass("pagination").isEmpty()) {
                    Element pagination = doc.getElementsByClass("pagination").first();
                    if (!pagination.getElementsContainingOwnText("»").isEmpty()) {
                        hasNextPage = true;
                        catUrl = "https://www.jainbookagency.com" + pagination.getElementsContainingOwnText("»").first().parent().attr("href");
                    } else {
                        hasNextPage = false;
                    }
                } else {
                    hasNextPage = false;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IBPBooksLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(JainBookLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }
}
