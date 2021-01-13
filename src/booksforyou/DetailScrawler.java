/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booksforyou;

import connectionManager.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khushbu
 */
public class DetailScrawler {
  static int current_thread_count = 0;
    static final int MAX_THREAD_COUNT = 5;
    public static void main(String[] args) {
        startScraping();
    }

    private static void startScraping() {       
    /*    String selectQuery = "select bk_id,url FROM books.booksforyou_links "
                + "where bk_id not in (SELECT url_id FROM books.booksforyou_books) group by url";*/
        String selectQuery = "select bk_id,url,category FROM books.booksforyou_links "
                + "where is_scraped=0";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(selectQuery);
        try {
            while (rs.next()) {
                String url = rs.getString("url");
                int id = rs.getInt("bk_id");
              //  String  category=rs.getString("category");
                DetailThread dt = new DetailThread(url, id);
                Thread th = new Thread(dt);
                th.start();
                 while (current_thread_count >= MAX_THREAD_COUNT) {
                    Thread.sleep(1000);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetailScrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
          Logger.getLogger(DetailScrawler.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
}
