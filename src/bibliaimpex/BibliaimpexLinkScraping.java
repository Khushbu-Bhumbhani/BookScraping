/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliaimpex;

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
public class BibliaimpexLinkScraping {

    public static void main(String[] args) {
        String catLinks[] = {
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Alternative%20systems%20of%20medicine&keywords=Alternative%20systems%20of%20medicine&Display=Alternative%20systems%20of%20medicine&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Anthropology&keywords=Anthropology&Display=Anthropology&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Archaeology&keywords=Archaeology&Display=Archaeology&showsubsubject=1&codestring=9788192450285",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Astrology&keywords=Astrology&Display=Astrology&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Astronomy&keywords=Astronomy&Display=Astronomy&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Ayurveda&keywords=Ayurveda&Display=Ayurveda&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Bibliographies&keywords=Bibliographies&Display=Bibliographies&showsubsubject=0&codestring=9789843390851",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Biography%20and%20Autobiography&keywords=Biography%20and%20Autobiography&Display=Biography%20and%20Autobiography&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Buddhism&keywords=Buddhism&Display=Buddhism&showsubsubject=1&codestring=9788194085010,%209788194085065,",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Census%20of%20India&keywords=Census%20of%20India&Display=Census%20of%20India&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Collected%20and%20Selected%20Works&keywords=Collected%20and%20Selected%20Works&Display=Collected%20and%20Selected%20Works&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Defence%20and%20Military%20Studies&keywords=Defence%20and%20Military%20Studies&Display=Defence%20and%20Military%20Studies&showsubsubject=0&codestring=9788194283782",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Dictionaries&keywords=Dictionaries&Display=Dictionaries&showsubsubject=0&codestring=9788193462188",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Ecology%20and%20Environment&keywords=Ecology%20and%20Environment&Display=Ecology%20and%20Environment&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Economics&keywords=Economics&Display=Economics&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Education&keywords=Education&Display=Education&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Epics&keywords=Epics&Display=Epics&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Fine%20Arts&keywords=Fine%20Arts&Display=Fine%20Arts&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Fisheries&keywords=Fisheries&Display=Fisheries&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Geography&keywords=Geography&Display=Geography&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Geology&keywords=Geology&Display=Geology&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Government/Institutional%20Serial%20Publications&keywords=Government/Institutional%20Serial%20Publications&Display=Government/Institutional%20Serial%20Publications&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=History&keywords=History&Display=History&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Indological%20Studies&keywords=Indological%20Studies&Display=Indological%20Studies&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Journalism&keywords=Journalism&Display=Journalism&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Law&keywords=Law&Display=Law&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Linguistics&keywords=Linguistics&Display=Linguistics&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Linguistics&keywords=Linguistics&Display=Linguistics&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Literature&keywords=Literature&Display=Literature&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Mathematics&keywords=Mathematics&Display=Mathematics&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Medicinal%20Plants&keywords=Medicinal%20Plants&Display=Medicinal%20Plants&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Memoirs&keywords=Memoirs&Display=Memoirs&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Music&keywords=Music&Display=Music,%20Dance,%20Drama%20and%20Cinema&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Natural%20History&keywords=Natural%20History&Display=Natural%20History&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Philosophy&keywords=Philosophy&Display=Philosophy&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Politics&keywords=Politics&Display=Politics&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Reference%20Works&keywords=Reference%20Works&Display=Reference%20Works&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Religion&keywords=Religion&Display=Religion&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Science%20and%20Technology&keywords=Science%20and%20Technology&Display=Science%20and%20Technology&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Sociology&keywords=Sociology&Display=Sociology&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=South%20Asian%20Studies&keywords=South%20Asian%20Studies&Display=South%20Asian%20Studies&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Tantra&keywords=Tantra&Display=Tantra&showsubsubject=1&codestring=9788184702118",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Texts%20M&keywords=Texts%20M&Display=Texts%20M&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Texts&keywords=Texts&Display=Texts&showsubsubject=1&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Yoga%20and%20Health&keywords=Yoga%20and%20Health&Display=Yoga%20and%20Health&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Zoology%20and%20Wild%20Life&keywords=Zoology%20and%20Wild%20Life&Display=Zoology%20and%20Wild%20Life&showsubsubject=0&codestring=",
            "https://www.bibliaimpex.com/index.php?p=sr&format=listpage&subject=Botany&keywords=Botany&Display=Botany&showsubsubject=0&codestring=",};
        for (String c : catLinks) {
            try {
                startLinkScrape(c);
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(BibliaimpexLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private static void startLinkScrape(String str) {
        boolean hasNextPage = false;
        String category = StringUtils.substringBetween(str, "&subject", "&keywords");
        category = category.replace("%20", " ");
        category = category.replace("=", "").trim();
        do {
            try {
                System.out.println("Getting..." + str);
                Document doc = Jsoup.connect(str).ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(0).get();

                for (Element e : doc.getElementsByClass("listbox")) {
                    String du = "https://www.bibliaimpex.com/" + e.getElementsByTag("a").first().attr("href");
                    String ISBN = StringUtils.substringBetween(e.html(), "ISBN:", "</p>");
                    // System.out.println("->" + du);
                    String insertQ = "INSERT INTO `books`.`bibliaimpex_links`\n"
                            + "(\n"
                            + "`url`,\n"
                            + "`ISBN`,\n"
                            + "`category`\n"
                            + ")\n"
                            + "VALUES\n"
                            + "("
                            + "'" + Utility.prepareString(du) + "',"
                            + "'" + Utility.prepareString(ISBN) + "',"
                            + "'" + Utility.prepareString(category) + "'"
                            + ");";
                    MyConnection.getConnection("books");
                    MyConnection.insertData(insertQ);
                    System.out.println("Inserted!");
                }
                if (!doc.getElementsContainingOwnText(">").isEmpty()) {
                    hasNextPage = true;
                    str = "https://www.bibliaimpex.com/" + doc.getElementsContainingOwnText(">").first().attr("href");
                } else {
                    hasNextPage = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(BibliaimpexLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }
}
