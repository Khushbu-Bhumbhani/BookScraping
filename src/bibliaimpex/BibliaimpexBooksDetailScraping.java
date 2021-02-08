/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliaimpex;

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
public class BibliaimpexBooksDetailScraping {

    static final int MAX_THREAD_COUNT = 3;
    static int CURRENT_THREAD_COUNT = 0;

    public static void main(String[] args) {
        MyConnection.getConnection("books");
        String selectQ = "SELECT bk_id,url FROM books.bibliaimpex_links where is_scraped=0";
        ResultSet rs = MyConnection.getResultSet(selectQ);
        try {
            while (rs.next()) {
                /*   detailScrape(rs.getString("url"), rs.getInt("bk_id"));
                System.out.println("Sleeping..");
                Thread.sleep(1000);*/
                DetailThread dh = new DetailThread(rs.getInt("bk_id"), rs.getString("url"));
                Thread thread = new Thread(dh);
                thread.start();
                while (CURRENT_THREAD_COUNT > MAX_THREAD_COUNT) {
                    try {
                        Thread.sleep(1000);
                        //  System.out.println("Sleeping..");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BibliaimpexBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (SQLException ex) {
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
            String editedOrTransdlatedBy = "";
            String language = "";
            String noOfPages = "";
            String binding = "";
            String year = "";
            String imageURL = "";
            String publisher = "";
            String ISBN10 = "";
            String ISBN13 = "";

            title = Utility.html2text(StringUtils.substringBetween(doc.html(), "Title:", "</strong>"));
            editedOrTransdlatedBy = Utility.html2text(StringUtils.substringBetween(doc.html(), "Edited/Translated by:", "</strong>"));
            author = Utility.html2text(StringUtils.substringBetween(doc.html(), "<p class=\"subheader text-left\">Author:", "</strong"));
            //    price = Utility.html2text(StringUtils.substringBetween(doc.html(), "Price :", "</p>"));
            year = Utility.html2text(StringUtils.substringBetween(doc.html(), "Year:", "</strong>"));
            binding = Utility.html2text(StringUtils.substringBetween(doc.html(), "Binding:", "</strong>"));
            publisher = Utility.html2text(StringUtils.substringBetween(doc.html(), "Publisher:", "</strong>"));
            language = Utility.html2text(StringUtils.substringBetween(doc.html(), "Language:", "</strong>"));
            ISBN10 = Utility.html2text(StringUtils.substringBetween(doc.html(), "ISBN 13:", "</strong>"));
            ISBN13 = Utility.html2text(StringUtils.substringBetween(doc.html(), "ISBN 10:", "</strong>"));
            //  ISBN = Utility.html2text(StringUtils.substringBetween(doc.html(), "ISBN :", "</p>"));
            noOfPages = Utility.html2text(StringUtils.substringBetween(doc.html(), "Pages etc.:", "</strong>"));

            if (!doc.getElementsByAttributeValueContaining("src", "/books/pics").isEmpty()) {
                imageURL = "https://www.bibliaimpex.com" + doc.getElementsByAttributeValueContaining("src", "/books/pics").first().attr("src");
            }
            String insertQ = "INSERT INTO `books`.`bibliaimpex_books`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`edited_or_translattedby`,\n"
                    + "`year`,\n"
                    + "`pages`,\n"
                    + "`binding`,\n"
                    + "`publisher`,\n"
                    + "`ISBN10`,\n"
                    + "`ISBN13`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`,\n"
                    + "`language`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + Utility.prepareString(title) + "',"
                    + "'" + Utility.prepareString(author) + "',"
                    + "'" + Utility.prepareString(editedOrTransdlatedBy) + "',"
                    + "'" + year + "',"
                    + "'" + noOfPages + "',"
                    + "'" + binding + "',"
                    + "'" + Utility.prepareString(publisher) + "',"
                    + "'" + ISBN10 + "',"
                    + "'" + ISBN13 + "',"
                    + "'" + imageURL + "',"
                    + "" + bkId + ","
                    + "'" + language + "'"
                    + ")";
            MyConnection.getConnection("books");
            if (MyConnection.insertData(insertQ)) {
                System.out.println("Inserted!!");
                String updateQ = "update books.bibliaimpex_links set is_scraped=1 where bk_id=" + bkId;
                MyConnection.insertData(updateQ);
                System.out.println("Updated!");
            }
        } catch (IOException ex) {
            Logger.getLogger(BibliaimpexBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
