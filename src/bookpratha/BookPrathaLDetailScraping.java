/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookpratha;

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

/**
 *
 * @author Khushbu
 */
public class BookPrathaLDetailScraping {

    public static void main(String[] args) {
        startScraping();
    }

    private static void startScraping() {
        try {
            //String url = "https://www.bookpratha.com/book/Bapuna-Ashramma-Gujarati-Book/111925";
            String query = "SELECT * FROM books.bookpratha_links where is_scraped=0";
            MyConnection.getConnection("books");
            ResultSet rs = MyConnection.getResultSet(query);
            while (rs.next()) {
                detailScrape("https://www.bookpratha.com"+rs.getString("url"), rs.getString("bk_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookPrathaLDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void detailScrape(String url, String url_id) {
        try {
          /*  System.out.println("" + url);
            String code = StringUtils.substringAfterLast(url, "/");
            url = url.replace("/" + code, "");
            String name = StringUtils.substringAfterLast(url, "/");
            url = "https://www.bookpratha.com/book/index/" + code + "?name=" + name;*/
            System.out.println("->" + url);

            Document doc = Jsoup.connect(url).ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0).get();

            String title = "";
            String author = "";
            String listprice = "";
            String publicationYear = "";
            String language = "";
            String pages = "";
            String paperback = "";
            String ISBN = "";
            String firstEdition = "";
            String imageURL = "";
            String binding = "";
           // System.out.println("" + doc.html());
            /* if (!doc.getElementsByAttributeValue("property", "offers").isEmpty() && !doc.getElementsByClass("price").isEmpty()) {

                listprice = doc.getElementsByAttributeValue("property", "offers").first().getElementsByClass("price").get(0).text();
            }*/
            listprice = StringUtils.substringBetween(doc.html(), "<p> <i class=\"fa fa-inr\"></i>", "<br");
            listprice = Utility.html2text(listprice);
            //     System.out.println(""+listprice);
            if (!doc.getElementsByClass("book-details-eng").isEmpty()) {
                title = doc.getElementsByClass("book-details-eng").first().getElementsByTag("h4").get(0).text();
            }
            /*if (doc.getElementById("ContentPlaceHolder1_tr_author") != null) {
                author = doc.getElementById("ContentPlaceHolder1_tr_author").getElementsByTag("span").get(2).text();
            }*/
            author = StringUtils.substringBetween(doc.html(), "Author :", "</a");
            author = Utility.html2text(author);
            /*if (doc.getElementById("ContentPlaceHolder1_tr_PubYear") != null) {
                publicationYear = doc.getElementById("ContentPlaceHolder1_tr_PubYear").getElementsByTag("span").get(2).text();
            }*/
            publicationYear = StringUtils.substringBetween(doc.html(), "Publication Year</h6>", "</h4");

            publicationYear = Utility.html2text(publicationYear);
            publicationYear = publicationYear.replace(":", "").trim();
            firstEdition = StringUtils.substringBetween(doc.html(), "First Edition</h6>", "</h4");
            firstEdition = Utility.html2text(firstEdition);
            firstEdition = firstEdition.replace(":", "").trim();

            ISBN = StringUtils.substringBetween(doc.html(), "ISBN", "</h4");
            ISBN = Utility.html2text(ISBN);
            ISBN = ISBN.replace(":", "").trim();

            pages = StringUtils.substringBetween(doc.html(), "Pages</h6>", "</h4");
            pages = Utility.html2text(pages);
            pages = pages.replace(":", "").trim();

            binding = StringUtils.substringBetween(doc.html(), "Binding</h6>", "</h4");
            binding = Utility.html2text(binding);
            binding = binding.replace(":", "").trim();

            paperback = StringUtils.substringBetween(doc.html(), "Paperback</h6>", "</h4");
            paperback = Utility.html2text(paperback);
            paperback = paperback.replace(":", "").trim();

            language = StringUtils.substringBetween(doc.html(), "Language</h6>", "</h4");

            language = Utility.html2text(language);
            language = language.replace(":", "").trim();
            /*   if (doc.getElementById("ContentPlaceHolder1_tr_FirstEd") != null) {
                firstEdition = doc.getElementById("ContentPlaceHolder1_tr_FirstEd").getElementsByTag("span").get(2).text();
            }
            if (doc.getElementById("ContentPlaceHolder1_tr_ISBN") != null) {
                ISBN = doc.getElementById("ContentPlaceHolder1_tr_ISBN").getElementsByTag("span").get(2).text();
            }
            if (doc.getElementById("ContentPlaceHolder1_tr_pages") != null) {
                pages = doc.getElementById("ContentPlaceHolder1_tr_pages").getElementsByTag("span").get(2).text();
            }
            if (doc.getElementById("ContentPlaceHolder1_tr_binding") != null) {
                binding = doc.getElementById("ContentPlaceHolder1_tr_binding").getElementsByTag("span").get(2).text();
            }
            if (doc.getElementById("ContentPlaceHolder1_tr_languageg") != null) {
                language = doc.getElementById("ContentPlaceHolder1_tr_language").getElementsByTag("span").get(2).text();
            }*/
            if (!doc.getElementsByClass("details").isEmpty()) {
                imageURL = doc.getElementsByClass("details").first().getElementsByTag("img").first().attr("src");
            }
            String insertQuery = "INSERT INTO `books`.`bookpratha_books`\n"
                    + "(\n"
                    + "`title`,\n"
                    + "`author`,\n"
                        + "`price`,\n"
                    + "`ISBN`,\n"
                    + "`first_edition`,\n"
                    + "`publication_year`,\n"
                    + "`pages`,\n"
                    + "`language`,\n"
                    + "`imageurl`,\n"
                    + "`url_id`,\n"
                    + "`paperback`,\n"
                    + "`book_url`,\n"
                    + "`binding`)\n"
                    + "VALUES\n"
                    + "("
                    + "'" + Utility.prepareString(title) + "',"
                    + "'" + Utility.prepareString(author) + "',"
                      + "'" + Utility.prepareString(listprice) + "',"
                    + "'" + Utility.prepareString(ISBN) + "',"
                    + "'" + Utility.prepareString(firstEdition) + "',"
                    + "'" + Utility.prepareString(publicationYear) + "',"
                    + "'" + Utility.prepareString(pages) + "',"
                    + "'" + Utility.prepareString(language) + "',"
                    + "'" + Utility.prepareString(imageURL) + "',"
                    + "'" + Utility.prepareString(url_id) + "',"
                    + "'" + Utility.prepareString(paperback) + "',"
                    + "'" + Utility.prepareString(url) + "',"
                    + "'" + Utility.prepareString(binding) + "'"
                    //+"'"+Utility.prepareString(title)+"',"
                    + ");";
            MyConnection.getConnection("books");
            MyConnection.insertData(insertQuery);
            System.out.println("Inserted!");
            String updateQ = "update `books`.`bookpratha_links` set is_scraped=1 where bk_id=" + url_id;
            MyConnection.insertData(updateQ);
        } catch (IOException ex) {
            Logger.getLogger(BookPrathaLDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
