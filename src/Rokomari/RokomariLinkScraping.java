/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import static Rokomari.RokomariLinkCrawler.current_thread_count;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import connectionManager.MyConnection;
import connectionManager.Utility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class RokomariLinkScraping implements Runnable {

    int id;
    String url;
    WebClient webClient;

    public RokomariLinkScraping(int id, String url) {
        this.id = id;
        this.url = url;
        current_thread_count++;

    }

    RokomariLinkScraping(int id, String url, WebClient webClient) {
        this.id = id;
        this.url = url;
        this.webClient = webClient;
        current_thread_count++;
    }

    @Override
    public void run() {
        scrapeLinks();
        current_thread_count--;
    }

    private void scrapeLinks() {
        boolean hasNextPage = false;

        do {
            System.out.println("Getting..." + url);

            try {
                HtmlPage page = webClient.getPage(url);
                Document doc = Utility.getWebDocument(page);

                /* Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .timeout(0)
                        .get();*/
                // System.out.println("" + doc.text());
                Element div = doc.getElementsByClass("browse__content").first();

                if (div == null) {
                    System.out.println("Null books:" + url);
                } else {
                    String q = "INSERT INTO `books`.`rokomari_link_all`\n"
                            + "(\n"
                            + "`url`,\n"
                            + "`category_id`)\n"
                            + "VALUES\n";
                    for (Element e : div.getElementsByClass("book-list-wrapper")) {
                        String detailURL = "https://www.rokomari.com" + e.getElementsByTag("a").first().attr("href");
                        System.out.println("" + detailURL);
                        q = q + " ('" + detailURL + "'," + id + "),";
                    }
                    q = q.substring(0, q.length() - 1);
                    insertQuery(q);
                }
                if (!doc.getElementsByClass("pagination").isEmpty() && !doc.getElementsByClass("pagination").first().getElementsContainingOwnText("Next").isEmpty()) {
                    Element nextLink=doc.getElementsByClass("pagination").first().getElementsContainingOwnText("Next").first();
                    if (nextLink.hasClass("disabled")) {
                        hasNextPage = false;
                    } else {
                        hasNextPage = true;
                        url = "https://www.rokomari.com" + nextLink.attr("href");
                    }
                } else {
                    hasNextPage = false;
                }

            } catch (IOException ex) {
                Logger.getLogger(RokomariLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        } while (hasNextPage);
        updateCategoryId();
    }

    private void insertQuery(String query) {
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("Inserted!!");
    }

    private void updateCategoryId() {
        String query = "update  books.rokomari_categories set is_scraped=1 where  category_id=" + id;
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("updated!!" + id);
    }

}
