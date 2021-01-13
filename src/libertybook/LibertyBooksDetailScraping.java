/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libertybook;

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
public class LibertyBooksDetailScraping {

    public static void main(String[] args) {
       startScraping();
    //      test();
    }

    private static void startScraping() {
        String selectQuery = "SELECT liberty_book_id,book_url FROM books.liberty_books_links_2020_urdu where is_scraped=0";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(selectQuery);
        try {
            while (rs.next()) {
                int id = rs.getInt("liberty_book_id");
                String url = rs.getString("book_url");
                detailScraping(id, url);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LibertyBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void detailScraping(int id, String url) {
        try {
            System.out.println("url:"+url);

            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            if (doc.text().contains("The page you requested cannot be found!")) {
                System.out.println("Page not found..." + url);
                String updateQuery = "update books.liberty_books_links_2020_urdu set is_scraped=-1 where liberty_book_id=" + id;
                MyConnection.insertData(updateQuery);
                System.out.println("status set to -1");
                return;
            }
            String title = "";
            String author = "";
            String imageURL = "";
            String publicationDate = "";
            String binding = "";
            String oldPrice = "";
            String newPrice = "";
            String noOfPages = "";
            String barCode = "";
            String publisher = "";
            String desc = "";

            if (!doc.getElementsByClass("heading-title").isEmpty()) {
                title = doc.getElementsByClass("heading-title").text();
                if (title.contains("By :")) {
                    title = StringUtils.substringBefore(title, "By :").trim();
                }

            }
            if (doc.getElementsByClass("product-page-content") != null) {
                Element pageContent = doc.getElementsByClass("product-page-content").first();
                if(pageContent!=null && !pageContent.getElementsByClass("image").isEmpty() && !pageContent.getElementsByClass("image").first().getElementsByTag("a").isEmpty())
                {
                imageURL = pageContent.getElementsByClass("image").first().getElementsByTag("a").first().attr("href");
                }
                Element div = doc.getElementById("product");
                if(div==null)
                {
                    System.out.println("null data..return");
                    return;
                }
                author = StringUtils.substringBetween(div.html(), "Author:", "</tr>");
                publicationDate = StringUtils.substringBetween(div.html(), "Publication Date:", "</tr>");
                binding = StringUtils.substringBetween(div.html(), "Binding:", "</tr>");
                if (!doc.getElementsByClass("price-old").isEmpty()) {
                    oldPrice = doc.getElementsByClass("price-old").first().text();
                }
                if (!doc.getElementsByClass("price-new").isEmpty()) {
                    newPrice = doc.getElementsByClass("price-new").first().text();
                }
                noOfPages = StringUtils.substringBetween(doc.html(), "Number of Pages:", "</tr>");
                barCode = StringUtils.substringBetween(doc.html(), "Bar Code:", "</tr>");
                publisher = StringUtils.substringBetween(doc.html(), "Publisher:", "</tr>");

                if (doc.getElementById("tab-description") != null) {
                    desc = StringUtils.substringBefore(doc.getElementById("tab-description").html(), "<h3 ");

                }
                desc = Utility.html2text(desc);
                author = Utility.html2text(author);
                publicationDate = Utility.html2text(publicationDate);
                binding = Utility.html2text(binding);
                noOfPages = Utility.html2text(noOfPages);
                barCode = Utility.html2text(barCode);
                publisher = Utility.html2text(publisher);
                // System.out.println("" + title + ";" + author + ";" + publicationDate + ";" + binding + ";" + oldPrice + ";" + newPrice + ";" + noOfPages + ";" + barCode + ";" + publisher);
                String insertQuery = "INSERT INTO `books`.`liberty_books_2020_urdu`\n"
                        + "(\n"
                        + "`title`,\n"
                        + "`author`,\n"
                        + "`listprice`,\n"
                        + "`originalprice`,\n"
                        + "`ISBN`,\n"
                        + "`edition`,\n"
                        + "`publisher`,\n"
                        + "`pages`,\n"
                        + "`imageurl`,\n"
                        + " `binding`,"
                        + "`desc`,"
                        + "`url_id`)\n"
                        + "VALUES\n"
                        + "("
                        + "'" + Utility.prepareString(title) + "',"
                        + "'" + Utility.prepareString(author) + "',"
                        + "'" + Utility.prepareString(newPrice) + "',"
                        + "'" + Utility.prepareString(oldPrice) + "',"
                        + "'" + Utility.prepareString(barCode) + "',"
                        + "'" + Utility.prepareString(publicationDate) + "',"
                        + "'" + Utility.prepareString(publisher) + "',"
                        + "'" + Utility.prepareString(noOfPages) + "',"
                        + "'" + Utility.prepareString(imageURL) + "',"
                        + "'" + Utility.prepareString(binding) + "',"
                        + "'" + Utility.prepareString(desc) + "',"
                        + id
                        + ");";
                //System.out.println(""+insertQuery);
                MyConnection.getConnection("books");
                boolean insertData = MyConnection.insertData(insertQuery);
                if (insertData) {
                    String updateQuery = "update books.liberty_books_links_2020_urdu set is_scraped=1 where liberty_book_id=" + id;
                    MyConnection.insertData(updateQuery);
                    System.out.println("Inserted!");
                }
            } else {
                System.out.println("product-page-content not found!");
            }
        } catch (IOException ex) {
            Logger.getLogger(LibertyBooksDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void test() {
        detailScraping(1, "https://www.libertybooks.com/Egyptomania:-A-History-Of-%C2%A0fascination,-Obsession-And-Fantasy");
    }

}
