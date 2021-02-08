/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientblackswan;

import bibliaimpex.*;
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
public class OrienblackswanBooksDetailScraping {

    public static void main(String[] args) {
        MyConnection.getConnection("books");
        String selectQ = "SELECT bk_id,url FROM books.orientblackswan_links where is_scraped=0";
        ResultSet rs = MyConnection.getResultSet(selectQ);
        try {
            while (rs.next()) {
                detailScrape(rs.getString("url"), rs.getInt("bk_id"));
                System.out.println("Sleeping..");
                Thread.sleep(2000);
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
            String language = "";
            String pages = "";
            String binding = "";
            String year = "";
            String imageURL = "";
            String publisher = "";
            String ISBN = "";

            title = doc.getElementById("ctl00_MainContent_dettitle").text();
            author = doc.getElementById("ctl00_MainContent_detauthor").text();
            price = doc.getElementById("ctl00_MainContent_detpricev").text().replace("Price", "").trim();
            language = doc.getElementById("ctl00_MainContent_detlangv").text().replace("Language", "").trim();
            pages = doc.getElementById("ctl00_MainContent_detpagev").text().replace("Pages", "").trim();
            binding = doc.getElementById("ctl00_MainContent_detbindv").text().replace("Format", "").trim();
            String dimention = doc.getElementById("ctl00_MainContent_detbsizev").text().replace("Dimensions", "").trim();
            String territory = doc.getElementById("ctl00_MainContent_dettrrv").text().replace("Territorial Rights", "").trim();
            String imprint = doc.getElementById("ctl00_MainContent_detimpv").text().replace("Imprint", "").trim();
            ISBN = doc.getElementById("ctl00_MainContent_detisbnv").text().replace("ISBN", "").trim();
            year = doc.getElementById("ctl00_MainContent_detyearv").text().replace("Year of Publishing", "").trim();

            if (doc.getElementById("myImg") != null) {
                imageURL = "https://orientblackswan.com" + doc.getElementById("myImg").attr("src");
            }
            
            String insertQ = "INSERT INTO `books`.`orientblackswan_books`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`price`,\n"
                    + "`year_of_publishing`,\n"
                    + "`pages`,\n"
                    + "`language`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`,\n"
                    + "`format`,\n"
                    + "`publisher`,\n"
                    + "`ISBN`,\n"
                    + "`dimensions`,\n"
                    + "`territorial_rights`,\n"
                    + "`imprint`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + Utility.prepareString(title) + "',"
                    + "'" + Utility.prepareString(author) + "',"
                    + "'" + Utility.prepareString(price) + "',"
                    + "'" + year + "',"
                    + "'" + pages + "',"
                    + "'" + language + "',"
                    + "'" + imageURL + "',"
                    + "" + bkId + ","
                    + "'" + binding + "',"
                    + "'" + Utility.prepareString(publisher) + "',"
                    + "'" + ISBN + "',"
                    + "'" + dimention + "',"
                    + "'" + Utility.prepareString(territory) + "',"
                    + "'" + Utility.prepareString(imprint) + "'"
                    + ")";
            MyConnection.getConnection("books");
            if (MyConnection.insertData(insertQ)) {
                System.out.println("Inserted!!");
                String updateQ = "update books.orientblackswan_links set is_scraped=1 where bk_id=" + bkId;
                MyConnection.insertData(updateQ);
                System.out.println("Updated!");
            }
        } catch (IOException ex) {
            Logger.getLogger(OrienblackswanBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
