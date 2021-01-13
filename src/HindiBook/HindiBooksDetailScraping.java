/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HindiBook;

import connectionManager.MyConnection;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Khushbu
 */
public class HindiBooksDetailScraping implements Runnable {

    int id;
    String url;

    public HindiBooksDetailScraping(int id, String url) {
        HindiBooksCrawler.CURRENT_THREAD_COUNT++;
        this.id = id;
        this.url = url;
    }

    @Override
    public void run() {
        detailScraping();
        HindiBooksCrawler.CURRENT_THREAD_COUNT--;
    }

    private void detailScraping() {
        try {
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            String title = "";
            String author = "";
            String listprice = "";
            String imageURL = "";
            String binding = "";
            String language = "";
            //  String category="";
            String annotation = "";
            String year = "";
            String pages = "";
            String ISBN10 = "";
            String ISBN13 = "";
            String yourPrice = "";
            String subjects = "";

            title = StringUtils.substringBetween(doc.html(), "Title:", "</p");
            author = StringUtils.substringBetween(doc.html(), "Author:", "</p");
            language = StringUtils.substringBetween(doc.html(), "Language:", "</p");
            year = StringUtils.substringBetween(doc.html(), "Year:", "</p");
            binding = StringUtils.substringBetween(doc.html(), "Binding:", "</p");
            pages = StringUtils.substringBetween(doc.html(), "Pages etc.:", "</p");
            ISBN13 = StringUtils.substringBetween(doc.html(), "ISBN 13:", "</p");
            ISBN10 = StringUtils.substringBetween(doc.html(), "ISBN 10:", "</p");
            subjects = StringUtils.substringBetween(doc.html(), "Subject(s):", "</p");
            // listprice = StringUtils.substringBetween(doc.html(), "List Price", "</p");
            if (!doc.getElementsByClass("orange").isEmpty()) {
                listprice = doc.getElementsByClass("orange").first().text();
            }
            imageURL = "https://www.hindibook.com" + doc.getElementsByClass("thumbnail").first().attr("src");
            //   annotation = StringUtils.substringBetween(doc.html(), "Annotation:", "</p");
            //  yourPrice = StringUtils.substringBetween(doc.html(), "Your Price", "</p");

            title = html2text(title);
            author = html2text(author);
            language = html2text(language);
            year = html2text(year);
            binding = html2text(binding);
            pages = html2text(pages);
            ISBN10 = html2text(ISBN10);
            ISBN13 = html2text(ISBN13);
            subjects = html2text(subjects);
            listprice = html2text(listprice);
            //  listprice=listprice.replaceAll(":", "");
            /* if(listprice.contains("Discount"))
            {
                listprice=StringUtils.substringBefore(listprice, "Discount");
            }
            annotation = html2text(annotation);
            yourPrice = html2text(yourPrice);*/

            System.out.println("" + url + ";" + ISBN10 + ";" + title + ";" + author + ";" + year + ";" + binding + ";" + pages + ";" + language + ";" + subjects + ";" + listprice + ";" + annotation + ";" + yourPrice);
            String insertQuery = "INSERT INTO `books`.`hindi_book_master`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                    + "`language`,\n"
                    + "`year`,\n"
                    + "`hb`,\n"
                    + "`pages`,\n"
                    + "`ISBN10`,\n"
                    + "`ISBN13`,\n"
                    + "`annotation`,\n"
                    + "`subject`,\n"
                    + "`list_price`,\n"
                    + "`your_price`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + prepareString(title) + "',"
                    + "'" + prepareString(author) + "',"
                    + "'" + prepareString(language) + "',"
                    + "'" + prepareString(year) + "',"
                    + "'" + prepareString(binding) + "',"
                    + "'" + pages + "',"
                    + "'" + ISBN10 + "',"
                    + "'" + ISBN13 + "',"
                    + "'" + prepareString(annotation) + "',"
                    + "'" + prepareString(subjects) + "',"
                    + "'" + listprice + "',"
                    + "'" + yourPrice + "',"
                    + "'" + imageURL + "',"
                    + "'" + id + "'"
                    + "); ";

            MyConnection.getConnection("books");
            MyConnection.insertData(insertQuery);
            System.out.println("Inserted!!");

            String updateQ = "update books.hindi_books_links set is_scraped=1 where hindi_url_id=" + id;
            MyConnection.insertData(updateQ);
        } catch (IOException ex) {
            Logger.getLogger(HindiBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
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
