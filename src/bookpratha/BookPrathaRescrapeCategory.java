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
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Khushbu
 */
public class BookPrathaRescrapeCategory {

    public static void main(String[] args) {
        try {
            //String url = "https://www.bookpratha.com/book/Bapuna-Ashramma-Gujarati-Book/111925";
            String query = "SELECT * FROM books.bookpratha_links where category=''";
            MyConnection.getConnection("books");
            ResultSet rs = MyConnection.getResultSet(query);
            while (rs.next()) {
                startScraping(rs.getString("url"), rs.getString("bk_id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookPrathaLDetailScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startScraping(String url, String url_id) {
        try {
            //String mainurl = "https://www.bookpratha.com/listing/Articles-Essays-Gujarati-Books/67";
            System.out.println("" + url);
            String code = StringUtils.substringAfterLast(url, "/");
            url = url.replace("/" + code, "");
            String name = StringUtils.substringAfterLast(url, "/");
            url = "https://www.bookpratha.com/book/index/" + code + "?name=" + name;
            System.out.println("->" + url);

            Document doc = Jsoup.connect(url).ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0).get();
            String category = "";
            if (!doc.getElementsByClass("inner-nav").isEmpty()) {
                category = doc.getElementsByClass("inner-nav").first().text();
                category = StringUtils.substringBeforeLast(category, ">");
                category = StringUtils.substringAfterLast(category, ">");
                System.out.println(">>" + category);
                String insertQ = "update bookpratha_links set category='" + Utility.prepareString(category) + "' where bk_id=" + url_id;
                MyConnection.getConnection("books");
                MyConnection.insertData(insertQ);
            }
            //  System.out.println("Inserted!!");
        } catch (IOException ex) {
            Logger.getLogger(BookPrathaRescrapeCategory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean waitForJSandJQueryToLoad(ChromeDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 30);

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    // return ((Long) ((JavascriptExecutor) getDriver()).executeScript("return jQuery.active") == 0);
                    return ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0").equals(true);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                // return ((JavascriptExecutor) getDriver()).executeScript("return document.readyState")
                //        .toString().equals("complete");
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }
}
