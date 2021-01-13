/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IBPbooks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import connectionManager.MyConnection;
/**
 *
 * @author Khushbu
 */
public class IBPBooksLinkScraping {

    static int website_id=1;
    public static void main(String[] args) {
        getCategoryList();
    }

    private static void startLinkScraping(String url) {
        // String url = "https://www.ibpbooks.com/books/biography-and-autobiography";
        boolean hasNextPage = false;
        do {
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                String insertQuery = "INSERT INTO `books`.`book_master`\n"
                        + "(\n"
                        + "`book_url`,website_source_id,page_url)\n"
                        + "VALUES\n";
                        
                for (Element card : doc.getElementsByClass("card-body")) {
                    if (!card.getElementsByTag("a").isEmpty()) {
                        insertQuery = insertQuery+" ('"+card.getElementsByTag("a").first().attr("href") + "',"
                                +website_id+",'"+url+"'),";
                        //System.out.println(""+detailURL);
                        
                    }
                }
               MyConnection.getConnection("books");
               MyConnection.insertData(insertQuery.substring(0, insertQuery.length()-1));
                //pagination
                if (!doc.getElementsByClass("pagination").isEmpty()) {
                    Element pagination = doc.getElementsByClass("pagination").first();
                    if (!pagination.getElementsContainingOwnText("»").isEmpty()) {
                        hasNextPage = true;
                        url = pagination.getElementsContainingOwnText("»").first().attr("href");
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
                Logger.getLogger(IBPBooksScraping.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }

    private static void getCategoryList() {
        String url = "https://www.ibpbooks.com/";
        try {
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            Element ul = doc.getElementsByClass("list-group").first();
            for (Element li : ul.getElementsByClass("list-group-item")) {
                //String mainCat=li.text();
                // System.out.println(""+mainCat);
                if (!li.getElementsByClass("subcategory-box").isEmpty()) {
                    String detailUrl = "";
                    for (Element e : li.getElementsByClass("subcategory-box").first().getElementsByTag("li")) {
                        detailUrl = e.getElementsByTag("a").first().attr("href");
                        System.out.println("Sub--->" + detailUrl);
                        startLinkScraping(detailUrl);
                    }
                } else {
                    String detailUrl = li.getElementsByTag("a").first().attr("href");
                    System.out.println("" + detailUrl);
                    startLinkScraping(detailUrl);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(IBPBooksLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
