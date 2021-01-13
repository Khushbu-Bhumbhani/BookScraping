/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import static Rokomari.RokomariLinkCrawler.current_thread_count;
import connectionManager.MyConnection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Khushbu
 */
public class updateCategories {
    public static void main(String[] args) {
        startUpdate();
    }

    private static void startUpdate() {
        String q="select b.url,rokomari_id from rokomari_unique_links_2020 l,rokomari_books_2020 b where url_id=link_id and (language like 'বাংলা%' or language like 'Bangla') and edition in ('2019','2020') and category='';";
         MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(q);
        try {
            while (rs.next()) {
                int id = rs.getInt("rokomari_id");
                String url = rs.getString("b.url");
                updateCategoryValue(id,url);
                Thread.sleep(1000);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RokomariLinkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(updateCategories.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    private static void updateCategoryValue(int id, String url) {
        try {
            System.out.println("Getting..." + url);
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            String category = "";
            if(!doc.getElementsByClass("ml-2").isEmpty())
            {
                category=doc.getElementsByClass("ml-2").first().text();
                System.out.println(""+category);
                String updateQ="update rokomari_books_2020 set category='"+category+"' where rokomari_id="+id;
                MyConnection.insertData(updateQ);
                System.out.println("Updated!");
            }
            else
            {
                System.out.println("Category not found...");
            }
        } catch (IOException ex) {
            Logger.getLogger(updateCategories.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
}
