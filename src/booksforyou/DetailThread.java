/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booksforyou;

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
public class DetailThread implements Runnable {

    String url;
    int id;

    public DetailThread(String url, int id) {
        this.url = url;
        this.id = id;
        DetailScrawler.current_thread_count++;

    }

    @Override
    public void run() {
        detailScrape();
        DetailScrawler.current_thread_count--;
    }

    private void detailScrape() {
        try {
           // url = url.replace("http://m.", "http://www.");
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            String title = "";
            String author = "";
            String listprice = "";
            String publication = "";
            String language = "";
            String pages = "";
            String format = "";
            String ISBN = "";
            String edition = "";
            String imageURL = "";

            if (!doc.getElementsByClass("book_list_top").isEmpty()) {
                Element div = doc.getElementsByClass("book_list_top").first();
                if (!div.getElementsByTag("b").isEmpty()) {
                    title = div.getElementsByTag("b").first().text();
                }
                if (!div.getElementsByTag("img").isEmpty()) {
                    imageURL = div.getElementsByTag("img").first().attr("src");
                }
                author = StringUtils.substringBetween(div.html(), "<strong>Author</strong>", "</tr>");
                listprice = StringUtils.substringBetween(div.html(), "<strong>Price</strong>", "</tr>");
                publication = StringUtils.substringBetween(div.html(), "<strong>Publisher</strong>", "</tr>");
                language = StringUtils.substringBetween(div.html(), "<strong>Language</strong>", "</tr>");
                format = StringUtils.substringBetween(div.html(), "<strong>Format</strong>", "</tr>");
                pages = StringUtils.substringBetween(div.html(), "<strong>No. Of Pages</strong>", "</tr>");
                ISBN = StringUtils.substringBetween(div.html(), "<strong>ISBN</strong>", "</tr>");
                edition = StringUtils.substringBetween(div.html(), "<strong>Edition</strong>", "</tr>");

                author = Utility.html2text(author);
                listprice = Utility.html2text(listprice);
                publication = Utility.html2text(publication);
                language = Utility.html2text(language);
                format = Utility.html2text(format);
                pages = Utility.html2text(pages);
                ISBN = Utility.html2text(ISBN);
                edition = Utility.html2text(edition);

                title = Utility.prepareString(title);
                author = Utility.prepareString(author);
                listprice = Utility.prepareString(listprice);
                publication = Utility.prepareString(publication);
                language = Utility.prepareString(language);
                format = Utility.prepareString(format);
                pages = Utility.prepareString(pages);
                ISBN = Utility.prepareString(ISBN);
                edition = Utility.prepareString(edition);

                String insertQuery = "INSERT INTO `books`.`booksforyou_books`\n"
                        + "(\n"
                        + "`title`,\n"
                        + "`author`,\n"
                        + "`price`,\n"
                        + "`ISBN`,\n"
                        + "`first_edition`,\n"
                        + "`publication_year`,\n"
                        + "`pages`,\n"
                        + "`language`,\n"
                        //      + "`country`,\n"
                        + "`imageurl`,\n"
                     //   + "`url`,\n"
                        + "`url_id`,\n"
                        + "`binding`)\n"
                        + "VALUES\n"
                        + "("
                        + "'" + title + "',"
                        + "'" + author + "',"
                        + "'" + listprice + "',"
                        + "'" + ISBN + "',"
                        + "'" + edition + "',"
                        + "'" + publication + "',"
                        + "'" + pages + "',"
                        + "'" + language + "',"
                        + "'" + imageURL + "',"
                       // + "'" + url + "',"
                        + "'" + id + "',"
                        + "'" + format + "'"
                        + ");";
                MyConnection.getConnection("books");
                MyConnection.insertData(insertQuery);
                System.out.println("Inserted");
                  updateId();
            }
        } catch (IOException ex) {
            Logger.getLogger(DetailThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateId() {
        String query = "update  books.booksforyou_links set is_scraped=1 where  bk_id=" + id;
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("updated!!" + id);
    }

}
