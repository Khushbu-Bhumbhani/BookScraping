/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orientblackswan;

import bibliaimpex.*;
import connectionManager.MyConnection;
import connectionManager.Utility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class OrientblackswanLinkScraping {

    public static void main(String[] args) {
        String catLinks[] = {
            "https://orientblackswan.com/books?id=37&sid=0&pid=0",
            "https://orientblackswan.com/books?id=9&sid=0&pid=0",
            "https://orientblackswan.com/books?id=39&sid=0&pid=0",
            "https://orientblackswan.com/books?id=13&sid=0&pid=0",
            "https://orientblackswan.com/books?id=2&sid=0&pid=0",
            "https://orientblackswan.com/books?id=5&sid=0&pid=0",
            "https://orientblackswan.com/books?id=3&sid=0&pid=0",
            "https://orientblackswan.com/books?id=19&sid=0&pid=0",
            "https://orientblackswan.com/books?id=15&sid=0&pid=0",
            "https://orientblackswan.com/books?id=27&sid=0&pid=0",
            "https://orientblackswan.com/books?id=6&sid=0&pid=0",
            "https://orientblackswan.com/books?id=7&sid=0&pid=0",
            "https://orientblackswan.com/books?id=24&sid=0&pid=0",
            "https://orientblackswan.com/books?id=8&sid=0&pid=0",
            "https://orientblackswan.com/books?id=21&sid=0&pid=0",
            "https://orientblackswan.com/books?id=44&sid=0&pid=0",
            "https://orientblackswan.com/books?id=17&sid=0&pid=0",
            "https://orientblackswan.com/books?id=28&sid=0&pid=0",
            "https://orientblackswan.com/books?id=14&sid=0&pid=0",
            "https://orientblackswan.com/books?id=33&sid=0&pid=0",
            "https://orientblackswan.com/books?id=25&sid=0&pid=0",
            "https://orientblackswan.com/books?id=31&sid=0&pid=0",
            "https://orientblackswan.com/books?id=36&sid=0&pid=0",
            "https://orientblackswan.com/books?id=0&sid=90&pid=0",
            "https://orientblackswan.com/books?id=34&sid=0&pid=0",
            "https://orientblackswan.com/books?id=38&sid=0&pid=0",
            "https://orientblackswan.com/books?id=22&sid=0&pid=0",
            "https://orientblackswan.com/books?id=16&sid=0&pid=0",
            "https://orientblackswan.com/books?id=35&sid=0&pid=0",
            "https://orientblackswan.com/books?id=40&sid=0&pid=0",
            "https://orientblackswan.com/books?id=41&sid=0&pid=0",
            "https://orientblackswan.com/books?id=43&sid=0&pid=0",
            "https://orientblackswan.com/books?id=46&sid=0&pid=0",
            "https://orientblackswan.com/books?id=42&sid=0&pid=0",
            "https://orientblackswan.com/books?id=18&sid=0&pid=0",
            "https://orientblackswan.com/books?id=20&sid=0&pid=0",
            "https://orientblackswan.com/subjectbooks?subid=1",
            "https://orientblackswan.com/subjectbooks?subid=2",
            "https://orientblackswan.com/subjectbooks?subid=3",
            "https://orientblackswan.com/subjectbooks?subid=4",
            "https://orientblackswan.com/subjectbooks?subid=5",
            "https://orientblackswan.com/subjectbooks?subid=6",
            "https://orientblackswan.com/subjectbooks?subid=7",
            "https://orientblackswan.com/subjectbooks?subid=8",
            "https://orientblackswan.com/subjectbooks?subid=9",
            "https://orientblackswan.com/subjectbooks?subid=10",
            "https://orientblackswan.com/subjectbooks?subid=11",
            "https://orientblackswan.com/subjectbooks?subid=12",
            "https://orientblackswan.com/subjectbooks?subid=13",
            "https://orientblackswan.com/subjectbooks?subid=14",
            "https://orientblackswan.com/subjectbooks?subid=15",
            "https://orientblackswan.com/subjectbooks?subid=16",
            "https://orientblackswan.com/subjectbooks?subid=17",
            "https://orientblackswan.com/books?id=4",
            "https://orientblackswan.com/books?id=26",
            "https://orientblackswan.com/books?id=30",
            "https://orientblackswan.com/books?id=12",
            "https://orientblackswan.com/books?id=1",
            "https://orientblackswan.com/books?id=10",
            "https://orientblackswan.com/books?id=11",
            "https://orientblackswan.com/books?id=32",
            "https://orientblackswan.com/books?id=23",};

        for (String c : catLinks) {
            try {
                startLinkScrape(c);
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(OrientblackswanLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private static void startLinkScrape(String str) {
        //  boolean hasNextPage = false;
        String category = "";
        //   String year = "";
        /*StringUtils.substringBetween(str, "&subject", "&keywords");
        category = category.replace("%20", " ");
        category = category.replace("=", "").trim();*/
        //  do {
        try {
            System.out.println("Getting..." + str);
            Document doc = Jsoup.connect(str).ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0).get();
            category = doc.getElementById("ctl00_MainContent_Heading").text();
            for (Element e : doc.getElementsByClass("booka")) {
                String du = "https://orientblackswan.com/" + e.attr("href");
                //     String ISBN = StringUtils.substringBetween(e.html(), "ISBN:", "</p>");
                // System.out.println("->" + du);
                String insertQ = "INSERT INTO `books`.`orientblackswan_links`\n"
                        + "(\n"
                        + "`url`,\n"
                        //  + "`year`,\n"
                        + "`category`\n"
                        + ")\n"
                        + "VALUES\n"
                        + "("
                        + "'" + Utility.prepareString(du) + "',"
                        //    + "'" + year + "',"
                        + "'" + Utility.prepareString(category) + "'"
                        + ");";
                MyConnection.getConnection("books");
                MyConnection.insertData(insertQ);
                System.out.println("Inserted!");
            }
            /*if (!doc.getElementsContainingOwnText(">").isEmpty()) {
                    hasNextPage = true;
                    str = "https://www.bibliaimpex.com/" + doc.getElementsContainingOwnText(">").first().attr("href");
                } else {
                    hasNextPage = false;
                }*/
        } catch (IOException ex) {
            Logger.getLogger(OrientblackswanLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
        }
        // } while (hasNextPage);
    }
}
