/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookpratha;

import connectionManager.MyConnection;
import connectionManager.Utility;
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
public class BookPrathaLinkScraping {

    public static void main(String[] args) {
        String urls[] = {
            //    "https://www.bookpratha.com/listing/Articles-Essays-Gujarati-Books/67",
            //   "https://www.bookpratha.com/sublisting/Astrology-Gujarati-Books/10119",
            //   "https://www.bookpratha.com/sublisting/Tarot-Gujarati-Books/30121",
            //    "https://www.bookpratha.com/sublisting/Vastushastra-Gujarati-Books/40130",
            //   "https://www.bookpratha.com/subsublisting/Autobiographies-of-Artists-Scientists-Sports-Personalities-Gujarati-Books/50083",
            //   "https://www.bookpratha.com/subsublisting/Autobiographies-of-Business-Entrepreneurs-Gujarati-Books/50093",
            //   "https://www.bookpratha.com/subsublisting/Historical-Political-Autobiographies-Gujarati-Books/10074",
            "https://www.bookpratha.com/Product_listing?MasterCatid=67",
            "https://www.bookpratha.com/Product_listing?MasterCatid=83",
            "https://www.bookpratha.com/Product_listing?MasterCatid=58",
            "https://www.bookpratha.com/Product_listing?MasterCatid=66",
            "https://www.bookpratha.com/Product_listing?MasterCatid=69",
            "https://www.bookpratha.com/Product_listing?MasterCatid=80",
            "https://www.bookpratha.com/Product_listing?MasterCatid=84",
            "https://www.bookpratha.com/Product_listing?MasterCatid=60",
            "https://www.bookpratha.com/Product_listing?MasterCatid=74",
            "https://www.bookpratha.com/Product_listing?MasterCatid=79",
            "https://www.bookpratha.com/Product_listing?MasterCatid=75",
            "https://www.bookpratha.com/Product_listing?MasterCatid=46",
            "https://www.bookpratha.com/Product_listing?MasterCatid=64",
            "https://www.bookpratha.com/Product_listing?MasterCatid=76",
            "https://www.bookpratha.com/Product_listing?MasterCatid=10084",
            "https://www.bookpratha.com/Product_listing?MasterCatid=71",
            "https://www.bookpratha.com/Product_listing?MasterCatid=10085",
            "https://www.bookpratha.com/Product_listing?MasterCatid=65",
            "https://www.bookpratha.com/Product_listing?MasterCatid=63",
            "https://www.bookpratha.com/Product_listing?MasterCatid=62",
            "https://www.bookpratha.com/Product_listing?MasterCatid=56",
            "https://www.bookpratha.com/Product_listing?MasterCatid=55",
            "https://www.bookpratha.com/Product_listing?MasterCatid=20083",
            "https://www.bookpratha.com/Product_listing?MasterCatid=77",
            "https://www.bookpratha.com/Product_listing?MasterCatid=68",
            "https://www.bookpratha.com/Product_listing?MasterCatid=20085",
            "https://www.bookpratha.com/Product_listing?MasterCatid=78",
            "https://www.bookpratha.com/Product_listing?MasterCatid=81",
            "https://www.bookpratha.com/Product_listing?MasterCatid=10083",
            "https://www.bookpratha.com/Product_listing?MasterCatid=30086",
            "https://www.bookpratha.com/Product_listing?MasterCatid=61",
            "https://www.bookpratha.com/Product_listing?MasterCatid=53",
            "https://www.bookpratha.com/Product_listing?MasterCatid=82",
            "https://www.bookpratha.com/Product_listing?MasterCatid=20084",};
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Khushbu\\Downloads\\chromedriver_win32(2)\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        for (String s : urls) {
            /*String category = "";
            if (s.contains("/listing/")) {
                category = StringUtils.substringBetween(s, "https://www.bookpratha.com/listing/", "/");
            } else if (s.contains("/sublisting/")) {
                category = StringUtils.substringBetween(s, "https://www.bookpratha.com/sublisting/", "/");
            }*/
            startScraping(s, driver);
        }
    }

    private static void startScraping(String mainurl, ChromeDriver driver) {
        //String mainurl = "https://www.bookpratha.com/listing/Articles-Essays-Gujarati-Books/67";
        String url = mainurl;

        WebDriverWait wait = new WebDriverWait(driver, 30);
        int pageNo = 1;
        int maxCount = 1;
        boolean hasNextPage = false;
        do {
            driver.get(url);
            waitForJSandJQueryToLoad(driver);

            System.out.println(">>>" + url);
            Document doc = Jsoup.parse(driver.getPageSource());
            String category = doc.getElementsByClass("inner-title").first().getElementsByTag("h1").first().text();
            //   Element id=doc.getElementById("ContentPlaceHolder1_content_datalist_newrelease");
            for (Element e : doc.getElementsByClass("product-list").first().getElementsByClass("hovereffect")) {
                String detailUrl = e.getElementsByTag("a").first().attr("href");
                //  System.out.println("" + detailUrl);
                String insertQ = "Insert into bookpratha_links (url,category) values ('" + Utility.prepareString(detailUrl) + "','" + Utility.prepareString(category) + "')";
                MyConnection.getConnection("books");
                MyConnection.insertData(insertQ);

                //  System.out.println("Inserted!!");
            }

            if (!doc.getElementsByClass("PagedList-pageCountAndLocation").isEmpty() && maxCount == 1) {
                String str = doc.getElementsByClass("PagedList-pageCountAndLocation").first().text();
                maxCount = Integer.parseInt(StringUtils.substringAfter(str, "of").trim());
                System.out.println("Total Pages:" + maxCount);
            }
            /*if (doc.getElementById("ContentPlaceHolder1_content_lnkNext") != null
                    && !doc.getElementById("ContentPlaceHolder1_content_lnkNext").hasClass("aspNetDisabled")) {

                JavascriptExecutor jse = (JavascriptExecutor) driver;

                //  jse.executeScript("scroll(250, 0)"); // if the element is on top.
                jse.executeScript("window.scrollTo(0, document.body.scrollHeight)"); // if the element is on bottom.

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder1_content_lnkNext")));
                driver.findElementById("ContentPlaceHolder1_content_lnkNext").click();
                waitForJSandJQueryToLoad(driver);
                hasNextPage = true;
            } else {
                hasNextPage = false;
            }*/
            pageNo++;
            url = mainurl + "&page=" + pageNo;
        } while (pageNo <= maxCount + 1);
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
