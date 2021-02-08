/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manoharbooks;

import connectionManager.MyConnection;
import connectionManager.Utility;
import gujaratibooks.GujratiBooksDetailScraping;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Khushbu
 */
public class ManoharBooksDetailScraping {

    public static void main(String[] args) {
        MyConnection.getConnection("books");
        String selectQ = "SELECT bk_id,url FROM books.manoharbooks_links where is_scraped=0";
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

    private static void detailScrape(String url, int bkId) {
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
            //String productCode = "";
            String publicationYear = "";
            //  String desc = "";
            String noOfPages = "";
            String edition = "";
            String ISBN = "";
            String imageURL = "";

            title = Utility.html2text(StringUtils.substringBetween(doc.html(), "Title :", "</tr>"));
            author = Utility.html2text(StringUtils.substringBetween(doc.html(), "Author :", "</tr>"));
            price = Utility.html2text(StringUtils.substringBetween(doc.html(), "Price :", "</tr>"));
            edition = Utility.html2text(StringUtils.substringBetween(doc.html(), "Edition :", "</tr>"));
            ISBN = Utility.html2text(StringUtils.substringBetween(doc.html(), "ISBN :", "</tr>"));
            noOfPages = Utility.html2text(StringUtils.substringBetween(doc.html(), "Pages :", "</tr>"));
            publicationYear = Utility.html2text(StringUtils.substringBetween(doc.html(), "Year of Pub :", "</tr>"));

            imageURL = "https://www.manoharbooks.com/" + doc.getElementsByAttributeValueContaining("src", "img/").first().attr("src");
            String insertQ = "INSERT INTO `books`.`manoharbooks_books`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`price`,\n"
                    + "`ISBN`,\n"
                    + "`publication_year`,\n"
                    + "`pages`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`,\n"
                    + "`edition`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + Utility.prepareString(title) + "',"
                    + "'" + Utility.prepareString(author) + "',"
                    + "'" + price + "',"
                    + "'" + ISBN + "',"
                    + "'" + publicationYear + "',"
                    + "'" + noOfPages + "',"
                    + "'" + imageURL + "',"
                    + "" + bkId + ","
                    + "'" + edition + "'"
                    + ")";
            MyConnection.getConnection("books");
            MyConnection.insertData(insertQ);
            System.out.println("Inserted!!");
            String updateQ = "update books.manoharbooks_links set is_scraped=1 where bk_id=" + bkId;
            MyConnection.insertData(updateQ);
            System.out.println("Updated!");
        } catch (IOException ex) {
            Logger.getLogger(ManoharBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
