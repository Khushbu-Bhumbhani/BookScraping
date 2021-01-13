/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HindiBook;

import IBPbooks.IBPCrawler;
import connectionManager.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khushbu
 */
public class HindiBooksCrawler {

    static final int MAX_THREAD_COUNT = 3;
    static int CURRENT_THREAD_COUNT = 0;

    public static void main(String[] args) {
        try {
            startCrawler();
        } catch (InterruptedException ex) {
            Logger.getLogger(HindiBooksCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startCrawler() throws InterruptedException {
        try {
            String selectQuery = "select hindi_url_id,book_url from hindi_books_links"
                    + " where is_scraped=0  group by book_url";
            MyConnection.getConnection("books");
            ResultSet rs = MyConnection.getResultSet(selectQuery);
            int i=0;
            while (rs.next()) {
                i++;
                int bookId = rs.getInt("hindi_url_id");
                String bookUrl = rs.getString("book_url");
                HindiBooksDetailScraping ibs = new HindiBooksDetailScraping(bookId,bookUrl);
                Thread thread = new Thread(ibs);
                thread.start();
                while (CURRENT_THREAD_COUNT > MAX_THREAD_COUNT) {
                    try {
                        Thread.sleep(1000);
                        //  System.out.println("Sleeping..");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(IBPCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               /* try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IBPCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                if(i>20)
                {
                    i=0;
                    System.out.println("sleeping");
                    Thread.sleep(3000);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HindiBooksCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
