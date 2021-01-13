/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gujaratibooks;

import connectionManager.MyConnection;
import connectionManager.Utility;
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
public class GujaratiBooksLinkScraping {

    public static void main(String[] args) {
        startScraping();
    }

    private static void startScraping() {
        try {
            //  String url = "https://www.gujaratibooks.com/";
            String url = "https://www.gujaratibooks.com/Gujarati-Books/";

            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();

            //System.out.println("" + doc.text());
            Element div = doc.getElementById("category-id-267");
            boolean flag = true;
            for (Element e : div.getElementsByClass("card-block")) {
                System.out.println("" + e.text());
                String categoriesLink = "https://www.gujaratibooks.com" + e.getElementsByTag("a").first().attr("href");
                System.out.println(">>>>>" + categoriesLink);
                String category = e.getElementsByTag("a").first().attr("title");
                if (categoriesLink.contains("general-knowledge")) {
                    flag = false;
                }
                if (flag) {
                    continue;
                }
                scrapeLinks(categoriesLink, category);
            }
        } catch (IOException ex) {
            Logger.getLogger(GujaratiBooksLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void scrapeLinks(String categoriesLink, String category) {
        boolean hasNextPage = false;
        do {
            // System.out.println("Category:" + categoriesLink);

            try {
                Document doc = Jsoup.connect(categoriesLink)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(0)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();

                if (doc.text().contains("There are no available products under this category. ")) {
                    //   System.out.println("No books in " + categoriesLink);
                } else {
                    Element div = doc.getElementsByClass("pcontainer").first();
                    if (div != null) {
                        for (Element e : div.getElementsByClass("card")) {
                            String link = "https://www.gujaratibooks.com" + e.getElementsByTag("a").first().attr("href");
                            System.out.println("->" + link);
                            String insertQ = "INSERT INTO `books`.`gujratibooks_links_2020`\n"
                                    + "(\n"
                                    + "`url`,\n"
                                    + "`category`)\n"
                                    + "VALUES\n"
                                    + "("
                                    + "'" + Utility.prepareString(link) + "',"
                                    + "'" + Utility.prepareString(category) + "'"
                                    + ")";
                            MyConnection.getConnection("books");
                            MyConnection.insertData(insertQ);
                        }
                    } else {
                        //   System.out.println("No books in container");
                    }

                }
                if (!doc.getElementsByClass("pagination").isEmpty() && !doc.getElementsByClass("pagination").first().getElementsByClass("fa-angle-right").isEmpty()) {

                    hasNextPage = true;
                    categoriesLink = "https://www.gujaratibooks.com" + doc.getElementsByClass("pagination").first().getElementsByClass("fa-angle-right").first().parent().attr("href");

                } else {
                    hasNextPage = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(GujaratiBooksLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (hasNextPage);
    }
}
