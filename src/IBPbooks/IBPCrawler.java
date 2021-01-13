/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IBPbooks;

import connectionManager.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khushbu
 */
public class IBPCrawler {

    static final int MAX_THREAD_COUNT = 4;
    static int CURRENT_THREAD_COUNT = 0;

    public static void main(String[] args) {
        startCrawling(args[0], args[1]);
    }

    private static void startCrawling(String start, String end) {
        String selectQuery = "";
        if (start != null && end != null) {
            selectQuery = "select book_master_id,book_url from book_master"
                    + " where is_scraped=0 and website_source_id=1 and  book_master_id between "+start+" and "+end;
        } else {
            selectQuery = "select book_master_id,book_url from book_master"
                    + " where is_scraped=0 and website_source_id=1 ";
        }
        // + "limit 0,1000";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(selectQuery);
        try {
            while (rs.next()) {
                int bookId = rs.getInt("book_master_id");
                String bookUrl = rs.getString("book_url");
                IBPBooksScraping ibs = new IBPBooksScraping(bookUrl, bookId);
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IBPCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(IBPCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
