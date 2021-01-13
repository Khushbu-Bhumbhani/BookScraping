/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import connectionManager.MyConnection;
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
public class Rokomari {
    
    public static void main(String[] args) {
        startScraping();
    }
    
    private static void startScraping() {
        String url = "https://www.rokomari.com/book/author/1/%E0%A6%B9%E0%A7%81%E0%A6%AE%E0%A6%BE%E0%A7%9F%E0%A7%82%E0%A6%A8-%E0%A6%86%E0%A6%B9%E0%A6%AE%E0%A7%87%E0%A6%A6?ref=mm_p0";
        System.out.println("--------------" + url);
        try {
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            
            for (Element e : doc.getElementsByClass("book-list-wrapper")) {
                //System.out.println(""+e.html());
                Element a = e.getElementsByClass("home-details-btn").first();
                String detailURL = "https://www.rokomari.com" + a.attr("href");
                System.out.println("" + detailURL);
               // scrapeDetails(detailURL);
            }
        } catch (IOException ex) {
            Logger.getLogger(Rokomari.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void scrapeDetails(String detailURL) {
        try {
            Document doc = Jsoup.connect(detailURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            String title = "";
            String author = "";
            String listprice = "";
            String publication = "";
            String language = "";
            String category = "";
            String summary = "";
            String bestseller = "";
            String pages = "";
            String ISBN = "";
            String edition = "";
            String orignalPrice = "";
            String country = "";
            String imageURL = "";
            
            if (!doc.getElementsByClass("details-book-main-info__header").isEmpty()) {
                title = doc.getElementsByClass("details-book-main-info__header").first().text();
            }
            if (!doc.getElementsByClass("details-book-info__content-author").isEmpty()) {
                author = doc.getElementsByClass("details-book-info__content-author").first().text();
            }
            if (!doc.getElementsByClass("original-price").isEmpty()) {
                orignalPrice = doc.getElementsByClass("original-price").first().text();
            }
            if (!doc.getElementsByClass("details-book-info__content-category best-seller-badge").isEmpty()) {
                bestseller = doc.getElementsByClass("details-book-info__content-category best-seller-badge").first().text();
            }
            if (!doc.getElementsByClass("look-inside").isEmpty()) {
                imageURL = doc.getElementsByClass("look-inside").first().attr("src");
            }
            
            if (!doc.getElementsByClass("details-book-info__content-book-price").isEmpty()) {
                Element div = doc.getElementsByClass("details-book-info__content-book-price").first();
                listprice = div.text();
                if (div.html().contains("<")) {
                    listprice = StringUtils.substringBefore(div.html(), "<");
                }
                /* else if(div.html().contains("<strike"))
                {
                    listprice=StringUtils.substringBefore(div.html(), "<strike");
                }*/
            }
            if (doc.getElementById("book-additional-content") != null && !doc.getElementById("book-additional-content").getElementsByClass("details-book-additional-info__content-summery").isEmpty()) {
                summary = doc.getElementById("book-additional-content").getElementsByClass("details-book-additional-info__content-summery").first().text();
            }
            category = StringUtils.substringBetween(doc.html(), "Category:", "</p>");
            edition = StringUtils.substringBetween(doc.html(), "Edition", "</tr>");
            publication = StringUtils.substringBetween(doc.html(), "Publisher", "</tr>");
            ISBN = StringUtils.substringBetween(doc.html(), "ISBN", "</tr>");
            pages = StringUtils.substringBetween(doc.html(), "Number of Pages", "</tr>");
            language = StringUtils.substringBetween(doc.html(), "Language", "</tr>");
            country = StringUtils.substringBetween(doc.html(), "Country", "</tr>");
            
            language = Utility.html2text(language);
            country = Utility.html2text(country);
            pages = Utility.html2text(pages);
            ISBN = Utility.html2text(ISBN);
            publication = Utility.html2text(publication);
            edition = Utility.html2text(edition);
            category = Utility.html2text(category);
            //    category=Utility.html2text(category);
            //     category=Utility.html2text(category);

            category = Utility.prepareString(category);
            edition = Utility.prepareString(edition);
            publication = Utility.prepareString(publication);
            ISBN = Utility.prepareString(ISBN);
            pages = Utility.prepareString(pages);
            language = Utility.prepareString(language);
            country = Utility.prepareString(country);
            summary = Utility.prepareString(summary);
            bestseller = Utility.prepareString(bestseller);
            title = Utility.prepareString(title);
            author = Utility.prepareString(author);

            // System.out.println("" + title + ";" + author + ";" + listprice + ";" + category + ";" + edition);
            /*  MyConnection.getConnection("books");
            String iq = "INSERT INTO `books`.`new_table`\n"
                    + "(`test_ name`,\n"
                    + "`test`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + title + "','" + author
                    + "');";
            MyConnection.insertData(iq);*/
            String insertQuery = "INSERT INTO `books`.`rokomari_books`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`listprice`,\n"
                    + "`originalprice`,\n"
                    + "`ISBN`,\n"
                    + "`edition`,\n"
                    + "`publisher`,\n"
                    + "`pages`,\n"
                    + "`language`,\n"
                    + "`country`,\n"
                    + "`category`,\n"
                    + "`imageurl`,\n"
                    + "`summary`,\n"
                    + "`url`,\n"
                    + "`is_scraped`,`bestseller`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + title + "',"
                    + "'" + author + "',"
                    + "'" + listprice + "',"
                    + "'" + orignalPrice + "',"
                    + "'" + ISBN + "',"
                    + "'" + edition + "',"
                    + "'" + publication + "',"
                    + "'" + pages + "',"
                    + "'" + language + "',"
                    + "'" + country + "',"
                    + "'" + category + "',"
                    + "'" + imageURL + "',"
                    + "'" + summary + "',"
                    + "'" + detailURL + "',"
                    + "'1',"
                    + "'" + bestseller + "'"
                    + ")";
            MyConnection.getConnection("books");
            MyConnection.insertData(insertQuery);
        } catch (IOException ex) {
            Logger.getLogger(Rokomari.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
