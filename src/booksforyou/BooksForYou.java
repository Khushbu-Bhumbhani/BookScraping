/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package booksforyou;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import connectionManager.MyConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Document;
import connectionManager.Utility;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class BooksForYou {

    public static void main(String[] args) {
        //    categoryLinks();
        startLinkScraping();
    }

    private static void categoryLinks() {
        try {
            Document doc = Jsoup.connect("http://www.booksforyou.co.in/").ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(0).get();
            Element ele = doc.getElementById("cate0HD");
            for (Element e : ele.getElementsByTag("a")) {
                //System.out.println("" + e.text());
                System.out.println("\"" + e.attr("href") + "/Gujarati\",");
            }
        } catch (IOException ex) {
            Logger.getLogger(BooksForYou.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void startScraping(String url) {
        //  String url = "http://m.booksforyou.co.in/mbook/Language/Gujarati";
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

        //Login
        HtmlPage page = null;
        boolean hasNextPage = true;
        int pageno = 1;
        try {
            page = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(5 * 1000);
            //System.out.println(""+page.asText());
            do {
                System.out.println("Page no.." + pageno);

                Document doc = Utility.getWebDocument(page);
                for (Element e : doc.getElementsByClass("books")) {
                    String link = e.getElementsByTag("a").first().attr("href");
                    String category = StringUtils.substringBeforeLast(url, "/");
                    category = StringUtils.substringAfterLast(category, "/");
                    insertLink(link, category);
                    //System.out.println("" + e.getElementsByTag("a").first().attr("href"));
                }
                if(doc.getElementsByClass("pagination").first().getElementsContainingOwnText("Last").first().hasAttr("disabled"))
                {
                    hasNextPage=false;
                    break;
                }
                pageno++;
                if ((pageno - 1) % 5 == 0) {
                    //  System.out.println(""+page.getByXPath("//dd[@class='pagination']//a[contains(text(),'...')]").size());
                    HtmlAnchor a = (HtmlAnchor) page.getByXPath("//dd[@class='pagination']//a[contains(text(),'Last')]").get(0);
                    a = (HtmlAnchor) a.getPreviousElementSibling();
                    page = a.click();
                    webClient.waitForBackgroundJavaScript(7 * 1000);
                    //  System.out.println(""+page.asXml());
                } else {
                    // System.out.println(""+doc.getElementsByClass("pagination").first().html());
                    //  System.out.println("pageno:" + pageno);
                    while (page.getByXPath("//dd[@class='pagination']//a[text()='" + pageno + "']").isEmpty()) {
                        System.out.println("Trying Again");
                        HtmlAnchor a = (HtmlAnchor) page.getByXPath("//dd[@class='pagination']//a[contains(text(),'Last')]").get(0);
                        a = (HtmlAnchor) a.getPreviousElementSibling();
                        page = a.click();
                        webClient.waitForBackgroundJavaScript(7 * 1000);
                    }
                    /*if (pageno % 10 == 0) {
                        webClient.waitForBackgroundJavaScript(3 * 1000);
                    }*/
                    HtmlAnchor a = (HtmlAnchor) page.getByXPath("//dd[@class='pagination']//a[text()='" + pageno + "']").get(0);
                    page = a.click();
                    webClient.waitForBackgroundJavaScript(7 * 1000);
                }
            } while (hasNextPage);
        } catch (IOException ex) {
            Logger.getLogger(BooksForYou.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(BooksForYou.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void startLinkScraping() {
        String str[] = {
         /*   "http://www.booksforyou.co.in/Categories/All-Time-Best-Sellers/Gujarati",
            "http://www.booksforyou.co.in/Categories/Alternative-Therapy/Gujarati",
            "http://www.booksforyou.co.in/Categories/Art-and-Design/Gujarati",
            "http://www.booksforyou.co.in/Categories/Articles-and-Essays/Gujarati",
            "http://www.booksforyou.co.in/Categories/Astrology/Gujarati",
            "http://www.booksforyou.co.in/Categories/Audio-Book/Gujarati",
            "http://www.booksforyou.co.in/Categories/Audio-CD/Gujarati",
            "http://www.booksforyou.co.in/Categories/Award-Winner/Gujarati",
            "http://www.booksforyou.co.in/Categories/Ayurveda/Gujarati", 
            "http://www.booksforyou.co.in/Categories/Beauty-and-Healthcare/Gujarati",
            "http://www.booksforyou.co.in/Categories/Biography/Gujarati",
            "http://www.booksforyou.co.in/Categories/Box-Set/Gujarati",
            "http://www.booksforyou.co.in/Categories/Business-and-Management/Gujarati",
            "http://www.booksforyou.co.in/Categories/Cancer/Gujarati",
            "http://www.booksforyou.co.in/Categories/Child-Psychology/Gujarati",
            "http://www.booksforyou.co.in/Categories/Children/Gujarati",
            "http://www.booksforyou.co.in/Categories/Cinema/Gujarati",
            "http://www.booksforyou.co.in/Categories/Civil-Services-Examination/Gujarati",
            "http://www.booksforyou.co.in/Categories/Classics/Gujarati",
            "http://www.booksforyou.co.in/Categories/Communication-Skills/Gujarati",
            "http://www.booksforyou.co.in/Categories/Competitive-Exams/Gujarati",
            "http://www.booksforyou.co.in/Categories/Computer/Gujarati",
            "http://www.booksforyou.co.in/Categories/Cookery/Gujarati",
            "http://www.booksforyou.co.in/Categories/Crafts-and-Hobbies/Gujarati",
            "http://www.booksforyou.co.in/Categories/Dictionaries/Gujarati",
            "http://www.booksforyou.co.in/Categories/Diet-and-Fitness/Gujarati",
            "http://www.booksforyou.co.in/Categories/Drama/Gujarati",
            "http://www.booksforyou.co.in/Categories/DVD/Gujarati",
            "http://www.booksforyou.co.in/Categories/Encyclopedia-and-Refrence/Gujarati",
            "http://www.booksforyou.co.in/Categories/English-Language/Gujarati",
            "http://www.booksforyou.co.in/Categories/Essay-and-Letter-Writing/Gujarati",
            "http://www.booksforyou.co.in/Categories/Fiction/Gujarati",
            "http://www.booksforyou.co.in/Categories/Foreign-Language-Learning/Gujarati",
            "http://www.booksforyou.co.in/Categories/Freedom-Fighters/Gujarati",
            "http://www.booksforyou.co.in/Categories/Gandhism/Gujarati",*/
            "http://www.booksforyou.co.in/Categories/General-Interest/Gujarati",
            "http://www.booksforyou.co.in/Categories/GPSC-EXAM/Gujarati",
            "http://www.booksforyou.co.in/Categories/Group-Discussion-and-Interviews/Gujarati",
            "http://www.booksforyou.co.in/Categories/Gujarati-Literature/Gujarati",
            "http://www.booksforyou.co.in/Categories/Health-Diet-and-Fitness/Gujarati",
            "http://www.booksforyou.co.in/Categories/Higher-Education/Gujarati",
            "http://www.booksforyou.co.in/Categories/History/Gujarati",
            "http://www.booksforyou.co.in/Categories/Humour/Gujarati",
            "http://www.booksforyou.co.in/Categories/Jokes-and-Riddles/Gujarati",
            "http://www.booksforyou.co.in/Categories/Journalism/Gujarati",
            "http://www.booksforyou.co.in/Categories/Literary-Fiction/Gujarati",
            "http://www.booksforyou.co.in/Categories/Magazine/Gujarati",
            "http://www.booksforyou.co.in/Categories/Medical-Science/Gujarati",
            "http://www.booksforyou.co.in/Categories/Mind-and-Memory/Gujarati",
            "http://www.booksforyou.co.in/Categories/Mind-Body-Spirit/Gujarati",
            "http://www.booksforyou.co.in/Categories/Nature/Gujarati",
            "http://www.booksforyou.co.in/Categories/Network-Marketing/Gujarati",
            "http://www.booksforyou.co.in/Categories/Non-Fiction/Gujarati",
            "http://www.booksforyou.co.in/Categories/Occult-Science/Gujarati",
            "http://www.booksforyou.co.in/Categories/Parenting/Gujarati",
            "http://www.booksforyou.co.in/Categories/Performing-Arts/Gujarati",
            "http://www.booksforyou.co.in/Categories/Personality-Development/Gujarati",
            "http://www.booksforyou.co.in/Categories/Philosophy/Gujarati",
            "http://www.booksforyou.co.in/Categories/Poetry-Gazals-Shayari/Gujarati",
            "http://www.booksforyou.co.in/Categories/Politics-and-Current-Affairs/Gujarati",
            "http://www.booksforyou.co.in/Categories/Popular-Science/Gujarati",
            "http://www.booksforyou.co.in/Categories/Pregnancy-and-Child-Care/Gujarati",
            "http://www.booksforyou.co.in/Categories/Psychology/Gujarati",
            "http://www.booksforyou.co.in/Categories/Quiz-and-Puzzles/Gujarati",
            "http://www.booksforyou.co.in/Categories/Quotations/Gujarati",
            "http://www.booksforyou.co.in/Categories/Rare-Books/Gujarati",
            "http://www.booksforyou.co.in/Categories/Refrence-Language/Gujarati",
            "http://www.booksforyou.co.in/Categories/Relationship/Gujarati",
            "http://www.booksforyou.co.in/Categories/Religion/Gujarati",
            "http://www.booksforyou.co.in/Categories/Sai-Baba/Gujarati",
            "http://www.booksforyou.co.in/Categories/Science-Fiction/Gujarati",
            "http://www.booksforyou.co.in/Categories/Science-Projects-and-Experiments/Gujarati",
            "http://www.booksforyou.co.in/Categories/Self-Help-and-Inspirational/Gujarati",
            "http://www.booksforyou.co.in/Categories/Short-Stories/Gujarati",
            "http://www.booksforyou.co.in/Categories/Sketching-and-Drawing/Gujarati",
            "http://www.booksforyou.co.in/Categories/Social-Media/Gujarati",
            "http://www.booksforyou.co.in/Categories/Speeches-and-Toasters/Gujarati",
            "http://www.booksforyou.co.in/Categories/Spiritual/Gujarati",
            "http://www.booksforyou.co.in/Categories/Sports-and-Leisure/Gujarati",
            "http://www.booksforyou.co.in/Categories/Stock-Market-and-Investment/Gujarati",
            "http://www.booksforyou.co.in/Categories/Study-Skill/Gujarati",
            "http://www.booksforyou.co.in/Categories/Travel-and-Travelogue/Gujarati",
            "http://www.booksforyou.co.in/Categories/Tribal-Literature/Gujarati",
            "http://www.booksforyou.co.in/Categories/Vastushastra-and-Fengshui/Gujarati",
            "http://www.booksforyou.co.in/Categories/Yoga-and-Meditation/Gujarati"};
        for (String link : str) {
            System.out.println("-->"+link);
            startScraping(link);
        }
    }

    private static void insertLink(String link, String category) {
        String insertQ = "INSERT INTO `books`.`booksforyou_links`\n"
                + "(\n"
                + "`url`,\n"
                + "`category`,\n"
                + "`scrape_no`\n"
                + ")\n"
                + "VALUES\n"
                + "("
                + "'" + link + "',"
                + "'" + category + "',"
                + "1" 
                + ");";
        MyConnection.getConnection("books");
        MyConnection.insertData(insertQ);
        System.out.println("Inserted!");
    }

}
