/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IBPbooks;

import static IBPbooks.IBPCrawler.CURRENT_THREAD_COUNT;
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
public class IBPBooksScraping implements Runnable {

    String detailURL;
    int bookId;

    public IBPBooksScraping(String detailURL, int bookId) {
        this.detailURL = detailURL;
        this.bookId = bookId;
        CURRENT_THREAD_COUNT++;
    }

    @Override
    public void run() {
        detailScrape();
        CURRENT_THREAD_COUNT--;
    }

    private void detailScrape() {
        String title = "";
        String cat = "";
        String auth = "";
        String imageURL = "";
        String ISBN = "";
        String year = "";
        String lang = "";
        String binding = "";
        String price = "";
        String desc = "";
        String subject = "";
        try {
            System.out.println("" + detailURL);
            Document doc = Jsoup.connect(detailURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            if (doc.text().contains("404 Page Not Found")) {
                String updateQuery = "UPDATE `books`.`book_master` set is_scraped=1  \n"
                        + "WHERE book_url = '" + detailURL + "'";
                MyConnection.getConnection("books");
                MyConnection.insertData(updateQuery);
            }
            Element div = doc.getElementsByClass("container-fluid").first();
            title = div.getElementsByClass("lead").first().text();
            auth = StringUtils.substringBetween(div.html(), "Author", "Specifications");
            ISBN = StringUtils.substringBetween(div.html(), "ISBN :", "</li>");
            year = StringUtils.substringBetween(div.html(), "year :", "</li>");
            lang = StringUtils.substringBetween(div.html(), "language :", "</li>");
            binding = StringUtils.substringBetween(div.html(), "binding :", "</li>");
            desc = StringUtils.substringBetween(div.html(), "<span class=\"text-muted\">Description</span>", "<div id=\"footer\">");
            // cat = doc.getElementsByClass("breadcrumb").first().text();
            for (Element e : doc.getElementsByClass("breadcrumb-item")) {
                if (cat.equals("")) {
                    cat = e.text();
                } else {
                    cat = cat + " / " + e.text();
                }
            }
            subject = StringUtils.substringBeforeLast(cat, "/");
            subject = StringUtils.substringAfterLast(subject, "/");
            subject = subject.trim();

            price = StringUtils.substringBetween(div.html(), "<span>Rs", "</span>");
            imageURL = div.getElementsByTag("img").first().attr("src");
            desc = html2text(desc);
            auth = html2text(auth);
            ISBN = html2text(ISBN);
            year = html2text(year);
            lang = html2text(lang);
            binding = html2text(binding);
            cat = html2text(cat);
            price = html2text(price);
            ISBN = ISBN.trim();
            // System.out.println(ISBN+"|"+title+"|"+price+"|"+auth+"|"+year+"|"+lang+"|"+binding+"|"+cat+"|"+desc+"|"+imageURL+"|"+detailURL);
            String updateQuery = "UPDATE `books`.`book_master`\n"
                    + "SET\n"
                    + "`book_title` = '" + prepareString(title) + "',\n"
                    + "`book_author` = '" + prepareString(auth) + "',\n"
                    + "`book_price` = '" + price + "',\n";
            if (!ISBN.matches("[0-9]+") && (ISBN.length() == 13 || ISBN.length() == 10)) {
                updateQuery = updateQuery + "`ISBN_X` = " + "'" + ISBN + "'" + ",\n";
            } else if (ISBN.matches("[0-9]+") && (ISBN.length() == 13 || ISBN.length() == 10)) {
                updateQuery = updateQuery + "`ISBN` = " + "'" + ISBN + "'" + ",\n";
            } else if (!ISBN.equals("")) {
                updateQuery = updateQuery + "`multiple_ISBN` = " + "'" + ISBN + "'" + ",\n";

            } else if (!ISBN.equals("")) {
                updateQuery = updateQuery + "`ISBN` =NULL,\n";

            }
            updateQuery = updateQuery + "`year` = " + (year.equals("") ? "NULL" : "'" + year + "'") + ",\n"
                    + "`binding` ='" + binding + "',\n"
                    + "`language` = '" + lang + "',\n"
                    + "`description` = '" + prepareString(desc) + "',\n"
                    + "`image_url` ='" + imageURL + "',\n"
                    + "`breadcrumb` = '" + prepareString(cat) + "',\n"
                    + "`is_scraped` = 1,\n"
                    + "`category_name` = '" + prepareString(subject) + "'\n"
                    + "WHERE book_url = '" + detailURL + "'";
            MyConnection.getConnection("books");
            MyConnection.insertData(updateQuery);
            System.out.println("Inserted!");
        } catch (IOException ex) {
            Logger.getLogger(IBPBooksScraping.class.getName()).log(Level.SEVERE, null, ex);
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
