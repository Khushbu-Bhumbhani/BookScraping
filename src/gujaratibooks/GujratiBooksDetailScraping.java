/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gujaratibooks;

import connectionManager.MyConnection;
import connectionManager.Utility;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GujratiBooksDetailScraping {

    public static void main(String[] args) {
        startScrape();
    }

    private static void startScrape() {
        // String link = "https://www.gujaratibooks.com/pauranik-natko-drama.html";
        MyConnection.getConnection("books");
        String selectQ = "SELECT bk_id,url FROM books.gujratibooks_links_2020 where is_scraped=0";
        ResultSet rs = MyConnection.getResultSet(selectQ);
        try {
            while (rs.next()) {
                detailScrape(rs.getString("url"), rs.getInt("bk_id"));
                System.out.println("Sleeping..");
                Thread.sleep(1000);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GujratiBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GujratiBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void detailScrape(String url, int urlId) {
        try {
            System.out.println("" + url);
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();

            String title = "";
            String author = "";
            String price = "";
            String productCode = "";
            String publicationYear = "";
            String desc = "";
            String noOfPages = "";
            String binding = "";
            String ISBN = "";
            String imageURL = "";

            title = doc.getElementsByClass("product-title-h1").first().text();

            Element div = doc.getElementsByClass("product-details").first();

            author = StringUtils.substringBetween(div.html(), "Author:", "</tr>");
            author = Utility.html2text(author);

            publicationYear = StringUtils.substringBetween(div.html(), "Publication Year:", "</tr>");
            publicationYear = Utility.html2text(publicationYear);

            noOfPages = StringUtils.substringBetween(div.html(), "Number of Pages:", "</tr>");
            noOfPages = Utility.html2text(noOfPages);

            binding = StringUtils.substringBetween(div.html(), "Binding:", "</tr>");
            binding = Utility.html2text(binding);

            ISBN = StringUtils.substringBetween(div.html(), "ISBN:", "</tr>");
            ISBN = Utility.html2text(ISBN);

            productCode = StringUtils.substringBetween(div.html(), "Product Code:", "</tr>");
            productCode = Utility.html2text(productCode);

            if (div.getElementById("product_price") != null) {
                price = div.getElementById("product_price").text();
            }
            if (div.getElementById("product_thumbnail") != null) {
                imageURL = div.getElementById("product_thumbnail").attr("data-src");
                if (imageURL.equalsIgnoreCase("/default_image.jpg")) {
                    imageURL = "";
                }
            }
            if (!doc.getElementsContainingOwnText("Description").isEmpty() && doc.getElementsContainingOwnText("Description").first().nextElementSibling() != null) {
                Element t = doc.getElementsContainingOwnText("Description").first().nextElementSibling();
                desc = t.text();
            }
            //    System.out.println("" + title + ";" + author + ";" + price + ";" + noOfPages + ";" + productCode + ";" + publicationYear + ";" + binding + ";" + ISBN + ";" + imageURL + ";" + desc);
            String insertQ = "INSERT INTO `books`.`gujartibooks_books_2020`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`price`,\n"
                    + "`ISBN`,\n"
                    + "`publication_year`,\n"
                    + "`pages`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`,\n"
                    + "`binding`,\n"
                    + "`desc`,\n"
                    + "`productcode`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + Utility.prepareString(title) + "',"
                    + "'" + Utility.prepareString(author) + "',"
                    + "'" + price + "',"
                    + "'" + ISBN + "',"
                    + "'" + publicationYear + "',"
                    + "'" + noOfPages + "',"
                    + "'" + imageURL + "',"
                    + "" + urlId + ","
                    + "'" + binding + "',"
                    + "'" + Utility.prepareString(desc) + "',"
                    + "'" + productCode + "'"
                    + ")";
            MyConnection.getConnection("books");
            MyConnection.insertData(insertQ);
            System.out.println("Inserted!!");
            String updateQ = "update books.gujratibooks_links_2020 set is_scraped=1 where bk_id=" + urlId;
            MyConnection.insertData(updateQ);
            System.out.println("Updated!");
        } catch (IOException ex) {
            Logger.getLogger(GujratiBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
