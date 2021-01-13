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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Khushbu
 */
public class RokomariLinkScrapeSelenium {

    public static void main(String[] args) {
        
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Khushbu\\Downloads\\chromedriver_win32(2)\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);

        String selectQ = "SELECT category_id,category_url FROM books.rokomari_categories_2020 where is_scraped=0";
        MyConnection.getConnection("books");
        ResultSet rs = MyConnection.getResultSet(selectQ);
        try {
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String url = rs.getString("category_url");
                startScrape(id, url,driver);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RokomariLinkScrapeSelenium.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startScrape(int id, String url, ChromeDriver driver) {
        // String url = "https://www.rokomari.com/book/category/1983/extra-discount?priceRange=0to56450&languageIds=1&discountRange=5to100";

        boolean hasNextPage = false;

        do {
            System.out.println("Getting..." + url);
            driver.get(url);
            // WebDriverWait wait = new WebDriverWait(driver, 5000);
            waitForJSandJQueryToLoad(driver);
            Document doc = Jsoup.parse(driver.getPageSource());
            //System.out.println("" + doc.text());
            // String detailURL="";
            Element div = doc.getElementsByClass("browse__content-books-wrapper").first();
            if (div == null) {
                System.out.println("Null books:" + url);
            } else {
                String q = "INSERT INTO `rokomari_link_master_2020`\n"
                        + "(\n"
                        + "`url`,\n"
                        + "`category_id`)\n"
                        + "VALUES\n";
                for (Element e : div.getElementsByClass("book-list-wrapper")) {
                    String detailURL = "https://www.rokomari.com/" + e.getElementsByTag("a").first().attr("href");
                    //  System.out.println("" + detailURL);
                    q = q + " ('" + detailURL + "'," + id + "),";
                }
                q = q.substring(0, q.length() - 1);
                insertQuery(q);
            }
            if (!doc.getElementsByClass("pagination").isEmpty() && !doc.getElementsByClass("pagination").first().getElementsContainingOwnText("Next").isEmpty()) {
                Element nextLink = doc.getElementsByClass("pagination").first().getElementsContainingOwnText("Next").first();
                if (nextLink.hasClass("disabled")) {
                    hasNextPage = false;
                } else {
                    hasNextPage = true;
                    url = "https://www.rokomari.com" + nextLink.attr("href");
                }
            } else {
                hasNextPage = false;
            }
        } while (hasNextPage);
        updateCategoryId(id);
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

    private static void insertQuery(String query) {
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("Inserted!!");
    }

    private static void updateCategoryId(int id) {
        String query = "update  books.rokomari_categories_2020 set is_scraped=1 where  category_id=" + id;
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("updated!!" + id);
    }
}
