/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import connectionManager.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khushbu
 */
public class RokomariDetailCrawler {

     static int current_thread_count = 0;
    static final int MAX_THREAD_COUNT = 1;

    public static void main(String[] args) {
        startScraping();
    }

    private static void startScraping() {
        String query = "select link_id,url from rokomari_unique_links_2020 where is_scraped=0";
        //String query = "select link_id,url FROM books.rokomari_link_master where link_id not in (select url_id from rokomari_books);";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(query);

        try {
            while (rs.next()) {
                int id = rs.getInt("link_id");
                String url = rs.getString("url");
                RokomariDetailScraping rd=new RokomariDetailScraping(id, url);
                 Thread thread = new Thread(rd);
                thread.start();
                while (current_thread_count >= MAX_THREAD_COUNT) {
                    Thread.sleep(1000);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RokomariDetailCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
             Logger.getLogger(RokomariDetailCrawler.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
