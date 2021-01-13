/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import connectionManager.MyConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khushbu
 */
public class RokomariLinkCrawler {

    static int current_thread_count = 0;
    static final int MAX_THREAD_COUNT =0;

    public static void main(String[] args) {
        // startCrawler();
        directCatgoryScrape();
    }

    private static void startCrawler() {
        String selectQ = "SELECT category_id,category_url FROM books.rokomari_categories where is_scraped=0";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(selectQ);
        try {
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String url = rs.getString("category_url");
                RokomariLinkScraping rss = new RokomariLinkScraping(id, url);
                Thread thread = new Thread(rss);
                thread.start();
                while (current_thread_count >= MAX_THREAD_COUNT) {
                    Thread.sleep(1000);
                }
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RokomariLinkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RokomariLinkCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void directCatgoryScrape() {
        String url = "https://www.rokomari.com/book/category/363/story?ref=act_pg0_p17&page=128";
        int id = 62856;
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        //   webClient.waitForBackgroundJavaScript(1000);

        webClient.getOptions()
                .setJavaScriptEnabled(true);
        webClient.setAjaxController(
                new NicelyResynchronizingAjaxController());
        webClient.getOptions()
                .setCssEnabled(false);
        webClient.getCookieManager()
                .setCookiesEnabled(true);
        webClient.getOptions()
                .setRedirectEnabled(true);
        webClient.getOptions()
                .setTimeout(0);
        webClient.getOptions()
                .setThrowExceptionOnScriptError(false);
        webClient.getOptions()
                .setThrowExceptionOnFailingStatusCode(false);
        RokomariLinkScraping rss = new RokomariLinkScraping(id, url,webClient);
        Thread thread = new Thread(rss);
        thread.start();
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
