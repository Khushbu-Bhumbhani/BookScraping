/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jainbookagecy;

import connectionManager.MyConnection;
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
public class JainBookDetailScraping implements Runnable {

    String detailURL;
    int bookId;
    String categoryName;

    public JainBookDetailScraping(String detailURL, int bookId, String category) {
        this.detailURL = detailURL;
        this.bookId = bookId;
        this.categoryName = category;
        JainBookCrawler.CURRENT_THREAD_COUNT++;
    }

    @Override
    public void run() {
        detailScrape();
        JainBookCrawler.CURRENT_THREAD_COUNT--;
    }

    private void detailScrape() {
        String title = "";
        //String cat = "";
        String auth = "";
        String imageURL = "";
        String ISBN = "";
        // String year = "";
        String edition = "";
        //  String binding = "";
        String price = "";
        String desc = "";
        //  String subject = "";
        String media = "";
        // String publication = "";

        try {
            System.out.println("" + detailURL);
            Document doc = Jsoup.connect(detailURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            /*  if (doc.text().contains("404 Page Not Found")) {
                String updateQuery = "UPDATE `books`.`book_master` set is_scraped=1 where \n"
                        + "WHERE book_url = '" + detailURL + "'";
                MyConnection.getConnection("books");
                MyConnection.insertData(updateQuery);
            }*/
            Element div = doc.getElementById("book_details");
            title = div.getElementsByClass("title").first().text();
            if (!div.getElementsByAttributeValueMatching("itemprop", "author").isEmpty()) {
                auth = div.getElementsByAttributeValueMatching("itemprop", "author").first().text();
            }
            if (!div.getElementsByAttributeValueMatching("itemprop", "bookEdition").isEmpty()) {
                edition = div.getElementsByAttributeValueMatching("itemprop", "bookEdition").first().text();
            }
            if (!div.getElementsByAttributeValueMatching("itemprop", "isbn").isEmpty()) {
                ISBN = div.getElementsByAttributeValueMatching("itemprop", "isbn").first().text();
            }
            if (!div.getElementsByAttributeValueMatching("itemprop", "description").isEmpty()) {
                desc = div.getElementsByAttributeValueMatching("itemprop", "description").first().text();
            }
            if (!div.getElementsByAttributeValueMatching("itemprop", "bookFormat").isEmpty()) {
                media = div.getElementsByAttributeValueMatching("itemprop", "bookFormat").first().text();
            }
            if (!div.getElementsByAttributeValueMatching("itemprop", "image").isEmpty()) {
                imageURL = "https://www.jainbookagency.com" + div.getElementsByAttributeValueMatching("itemprop", "image").first().attr("src");
            }
            if (!div.getElementsByClass("price").isEmpty()) {
                price = div.getElementsByClass("price").first().text();
            }
            String availbility = "";
            if (div.text().contains("Currently not available")) {
                availbility = "Not available";
            } else {
                availbility = "Available";
            }
            // auth = StringUtils.substringBetween(div.html(), "Author", "Specifications");
            String JBABookCode = StringUtils.substringBetween(div.html(), "JBA Book Code : ", "</div>");
            // desc = StringUtils.substringBetween(div.html(), "<span class=\"text-muted\">Description</span>", "<div id=\"footer\">");

            //    price = StringUtils.substringBetween(div.html(), "<span>Rs", "</span>");
            // imageURL = div.getElementsByTag("img").first().attr("src");
            desc = html2text(desc);
            // auth = html2text(auth);
            //ISBN = html2text(ISBN);

            price = html2text(price);
            JBABookCode = html2text(JBABookCode);
            ISBN = ISBN.trim();
            //  System.out.println(ISBN + "|" + title + "|" + price + "|" + auth + "|" + media + "|" + edition + "|" + desc + "|" + imageURL + "|" + detailURL + " "
            //            + "| " + JBABookCode);
            String insertQuery = "INSERT INTO `books`.`jain_books_master`\n"
                    + "(\n"
                    + "`book_title`,\n"
                    + "`book_author`,\n"
                    + "`book_price`,\n";
            if (!ISBN.matches("[0-9]+") && (ISBN.length() == 13 || ISBN.length() == 10)) {
                insertQuery = insertQuery + "ISBN_X,\n";
            } else if (ISBN.matches("[0-9]+") && (ISBN.length() == 13 || ISBN.length() == 10)) {
                insertQuery = insertQuery + "ISBN,\n";
            } else if (!ISBN.equals("")) {
                insertQuery = insertQuery + "multiple_ISBN,\n";

            }
            insertQuery = insertQuery + "`description`,\n"
                    + "`image_url`,\n"
                    + "`edition`,\n"
                    + "`media`,\n"
                    + "`JBA_book_code`,\n"
                    + "`website_source_id`,\n"
                    + "`category_name`,\n"
                    + "`availability`,\n"
                    + "`jain_books_link_master_id`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + prepareString(title) + "',"
                    + "'" + prepareString(auth) + "',"
                    + "'" + price + "',"
                    + (ISBN.equals("") ? "NULL" : "'" + ISBN + "'") + ","
                    + "'" + prepareString(desc) + "',"
                    + "'" + imageURL + "',"
                    + "'" + prepareString(edition) + "',"
                    + "'" + prepareString(media) + "',"
                    + "'" + JBABookCode + "',"
                    + "'" + 2 + "',"
                    + "'" + prepareString(categoryName) + "',"
                    + "'" + availbility + "',"
                    + "'" + bookId + "'"
                    + ");";

            MyConnection.getConnection("books");
            MyConnection.insertData(insertQuery);
            System.out.println("Inserted!");

            String update = "update jain_books_link_master set is_scraped=1 where jain_books_master_id=" + bookId;
            MyConnection.insertData(update);
        } catch (IOException ex) {
            Logger.getLogger(JainBookLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String prepareString(String str) {
        if (str != null) {
            str = str.replaceAll("\'", "\''");
            str = str.replace("\\", "\\\\");
        } else {
            return "";
        }
        return str;
    }

    public static String html2text(String html) {
        if (html == null) {
            return "";
        }
        if (!html.equals("")) {
            return Jsoup.parse(html).text().trim();
        } else {
            return html;

        }
    }

}
