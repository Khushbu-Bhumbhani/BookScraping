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
public class RokomariDetailScraping implements Runnable {

    int id;
    String url;

    public RokomariDetailScraping(int id, String url) {
        this.id = id;
        this.url = url;
        RokomariDetailCrawler.current_thread_count++;

    }

    @Override
    public void run() {
        detailScrape();
        RokomariDetailCrawler.current_thread_count--;
    }

    private void detailScrape() {
        try {
            System.out.println("Getting..." + url);
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            String title = "";
            String author = "";
            String listprice = "";
            String sellPrice = "";
            String publication = "";
            String language = "";
        String category = "";
            String summary = "";
            String bestseller = "";
            String pages = "";
            String ISBN = "";
            String edition = "";
            String editionnew = "";
            String orignalPrice = "";
            String country = "";
            String imageURL = "";

            if (!doc.getElementsByClass("original-price").isEmpty()) {
                orignalPrice = doc.getElementsByClass("original-price").first().text();
            }
            if (!doc.getElementsByClass("details-book-info__content-category best-seller-badge").isEmpty()) {
                bestseller = doc.getElementsByClass("details-book-info__content-category best-seller-badge").first().text();
            }
            if (!doc.getElementsByClass("look-inside").isEmpty()) {
                imageURL = doc.getElementsByClass("look-inside").first().attr("src");
            } else if (!doc.getElementsByClass("details-book-main-img-wrapper").isEmpty()) {
                if (!doc.getElementsByClass("details-book-main-img-wrapper").first().getElementsByTag("img").isEmpty()) {
                    imageURL = doc.getElementsByClass("details-book-main-img-wrapper").first().getElementsByTag("img").attr("src");
                }
            }

            if (!doc.getElementsByClass("details-book-info__content-book-price").isEmpty()) {
                Element div = doc.getElementsByClass("details-book-info__content-book-price").first();
                // listprice = div.text();
                /* if (div.html().contains("<")) {
                    listprice = StringUtils.substringBefore(div.html(), "<");
                }*/
 /* else if(div.html().contains("<strike"))
                {
                    listprice=StringUtils.substringBefore(div.html(), "<strike");
                }*/
                
                if (!div.getElementsByClass("sell-price").isEmpty()) {
                    sellPrice = div.getElementsByClass("sell-price").text();
                }
            }
            if (doc.getElementById("book-additional-content") != null && !doc.getElementById("book-additional-content").getElementsByClass("details-book-additional-info__content-summery").isEmpty()) {
                summary = doc.getElementById("book-additional-content").getElementsByClass("details-book-additional-info__content-summery").first().text();
            }
            category = StringUtils.substringBetween(doc.html(), "<span>Category:", "</p>");
            author = StringUtils.substringBetween(doc.html(), " <td>Author", "</tr>");
            title = StringUtils.substringBetween(doc.html(), " <td>Title", "</tr>");
            edition = StringUtils.substringBetween(doc.html(), "<td>Edition", "</tr>");
            publication = StringUtils.substringBetween(doc.html(), "<td>Publisher", "</tr>");
            ISBN = StringUtils.substringBetween(doc.html(), "<td>ISBN", "</tr>");
            pages = StringUtils.substringBetween(doc.html(), "<td>Number of Pages", "</tr>");
            language = StringUtils.substringBetween(doc.html(), "<td>Language", "</tr>");
            country = StringUtils.substringBetween(doc.html(), "<td>Country", "</tr>");

            language = Utility.html2text(language);
            author = Utility.html2text(author);
            title = Utility.html2text(title);
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

            if (edition.contains(",")) {
                editionnew = StringUtils.substringAfter(edition, ",").trim();
            } else {
                editionnew = edition;
            }
            if (author.equals("")) {
                if (!doc.getElementsByClass("details-book-info__content-author").isEmpty()) {
                    author = doc.getElementsByClass("details-book-info__content-author").first().text();
                }
            }
            if (title.equals("")) {
                if (!doc.getElementsByClass("details-book-main-info__header").isEmpty()) {
                    title = doc.getElementsByClass("details-book-main-info__header").first().text();
                }
            }
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
            String insertQuery = "INSERT INTO `books`.`rokomari_books_2020`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`sellprice`,\n"
                    + "`originalprice`,\n"
                    + "`ISBN`,\n"
                    + "`edition_original`,`edition`,\n"
                    + "`publisher`,\n"
                    + "`pages`,\n"
                    + "`language`,\n"
                    + "`country`,\n"
                    + "`category`,\n"
                    + "`imageurl`,\n"
                    + "`summary`,\n"
                    + "`url`,`url_id`,\n"
                    + "`is_scraped`,`bestseller`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + title + "',"
                    + "'" + author + "',"
                    + "'" + sellPrice + "',"
                    + "'" + orignalPrice + "',"
                    + "'" + ISBN + "',"
                    + "'" + edition + "','" + editionnew + "',"
                    + "'" + publication + "',"
                    + "'" + pages + "',"
                    + "'" + language + "',"
                    + "'" + country + "',"
                    + "'" + category + "',"
                    + "'" + imageURL + "',"
                    + "'" + summary + "',"
                    + "'" + url + "'," + id
                    + ",'1',"
                    + "'" + bestseller + "'"
                    + ")";
            MyConnection.getConnection("books");
            System.out.println("urlid:" + id);
            MyConnection.insertData(insertQuery);
            //  System.out.println("Inserted");
             updateId();
        } catch (IOException ex) {
            Logger.getLogger(Rokomari.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateId() {
        String query = "update rokomari_unique_links_2020 set is_scraped=1 where  link_id=" + id;
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("updated!!" + id);
    }

}
