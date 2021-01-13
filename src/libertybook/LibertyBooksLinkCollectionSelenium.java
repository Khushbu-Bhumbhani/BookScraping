/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libertybook;

import static Rokomari.RokomariLinkScrapeSelenium.waitForJSandJQueryToLoad;
import connectionManager.MyConnection;
import connectionManager.Utility;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Khushbu
 */
public class LibertyBooksLinkCollectionSelenium {
//

    public static void main(String[] args) {
        crawlCategoryURLs();
    }

    private static void crawlCategoryURLs() {
        String urls[] = {
            "https://www.libertybooks.com/index.php?route=product/category&path=420",
            "https://www.libertybooks.com/index.php?route=product/category&path=782",
            "https://www.libertybooks.com/index.php?route=product/category&path=427",
            "https://www.libertybooks.com/index.php?route=product/category&path=414",
            "https://www.libertybooks.com/index.php?route=product/category&path=963",
            "https://www.libertybooks.com/index.php?route=product/category&path=428",
            "https://www.libertybooks.com/index.php?route=product/category&path=438",
            "https://www.libertybooks.com/index.php?route=product/category&path=964",
            "https://www.libertybooks.com/index.php?route=product/category&path=791"};

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Khushbu\\Downloads\\chromedriver_win32(2)\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        for (String u : urls) {
            startLinkScrapingSelenium(u, driver);
        }
        driver.close();
    }

    private static void startLinkScrapingSelenium(String url, ChromeDriver driver) {
        System.out.println("Getting..." + url);
        driver.get(url);
        waitForJSandJQueryToLoad(driver);
        boolean hasNextPage = false;
        do {
            Document doc = Jsoup.parse(driver.getPageSource());
            String category = doc.getElementsByClass("heading-title").first().text();
            String breadcrumbs = doc.getElementsByClass("breadcrumb").first().html();
            breadcrumbs = breadcrumbs.replaceAll("</li>", " | ").trim();
            breadcrumbs = breadcrumbs.substring(0, breadcrumbs.length() - 1).trim();
            breadcrumbs = Utility.html2text(breadcrumbs);

            Element div = doc.getElementsByClass("main-products").first();
            if (div != null) {
                for (Element e : div.getElementsByClass("product-grid-item")) {
                    String detailurl = e.getElementsByClass("name").first().getElementsByTag("a").first().attr("href");
                    System.out.println("" + detailurl);
                    String insertQ = "INSERT INTO `books`.`liberty_books_links_2020_urdu`\n"
                            + "(\n"
                            + "`book_url`,\n"
                            + "`category`,`breadcrumb`)\n"
                            + "VALUES\n"
                            + " ('" + Utility.prepareString(detailurl) + "','" + Utility.prepareString(category) + "','" + Utility.prepareString(breadcrumbs) + "')";
                    MyConnection.getConnection("books");
                    MyConnection.insertData(insertQ);
                }

                //pagination
                if (driver.findElementsByLinkText(">").size() > 0) {
                    WebElement a = driver.findElementsByLinkText(">").get(0);
                    if (a != null) {
                        System.out.println("Getting..." + a.getAttribute("href"));
                        driver.get(a.getAttribute("href"));
                        hasNextPage = true;
                    } else {
                        hasNextPage = false;
                    }

                } else {
                    hasNextPage = false;
                }
            }
        } while (hasNextPage);
    }

    /* private static void startLinkScraping(String url) {
        // String url = "https://www.libertybooks.com/index.php?route=product/category&path=189";
        boolean hasNextPage = false;
        int pageno = 2;

        do {
            System.out.println("Getting..." + url);
            try {
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                String category = doc.getElementsByClass("heading-title").first().text();
                String breadcrumbs = doc.getElementsByClass("breadcrumb").first().html();
                breadcrumbs = breadcrumbs.replaceAll("</li>", " | ").trim();
                breadcrumbs = breadcrumbs.substring(0, breadcrumbs.length() - 1).trim();
                breadcrumbs = Utility.html2text(breadcrumbs);

                Element div = doc.getElementsByClass("main-products").first();
                for (Element e : div.getElementsByClass("product-grid-item")) {
                    String detailurl = e.getElementsByTag("a").first().attr("href");
                    System.out.println("" + detailurl);
                }
               
            } catch (IOException ex) {
                Logger.getLogger(LibertyBooksLinkCollectionSelenium.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }*/
}
