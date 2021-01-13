/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unistarbooks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class UniStarBooks {

    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) {
    //    startLinkScraping();
        doProductScraping();
    }

    private static void startLinkScraping() {
        String authorListURL = "http://www.unistarbooks.com/5_punjabi-books";
        try {
            Document doc = Jsoup.connect(authorListURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();

            Element div = doc.getElementById("subcategories");
            for (Element e : div.getElementsByTag("a")) {
                System.out.println("->Athur:" + e.text());
                String bookListURL = "http://www.unistarbooks.com/" + e.attr("href");
                System.out.println("-->author url:" + bookListURL);
                if (!bookListURL.contains("search_query=&")) {
                    scrapeLinks(bookListURL);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UniStarBooks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void scrapeLinks(String bookListURL) {
        try {
            Document doc = Jsoup.connect(bookListURL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            if (doc.text().contains("No results were found for your search")) {
                System.out.println("NO RESULT..");
                return;
            }
            if (doc.getElementById("product_list") != null) {
                Element bookList = doc.getElementById("product_list");
                for (Element e : bookList.getElementsByClass("ajax_block_product")) {
                    String detailURL = "http://www.unistarbooks.com/" + e.getElementsByTag("a").first().attr("href") + LINE_SEPARATOR;
                    //    System.out.println("" + detailURL);
                    Files.write(Paths.get("C:\\Users\\Khushbu\\Desktop\\UnistarLinks.txt"), detailURL.getBytes(), StandardOpenOption.APPEND);
                }
            } else {
                System.out.println(">>>>>>>>> NO Product list");
            }
        } catch (IOException ex) {
            System.out.println("Exception: " + bookListURL);
            // Logger.getLogger(UniStarBooks.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error:" + ex);
        }
    }

    private static void doProductScraping() {
        String str[] = {
            "http://www.unistarbooks.com/search?controller=search&search_query=A.S Dulat&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Amandeep Sandhu&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Amandeep Singh Aman&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Amrik Singh Dayal&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Balbir Kaur Sanghera&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Balbir Parwana&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Balbir Singh Bir&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Baldev Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Bhagwant Singh (Dr.)&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Deepak Kumar&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dharminder Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr. Sharanjit Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Hari Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Harinder Singh Turh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Harman Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Harpreet Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Jagjit Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Jagminder Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Jasbir Singh Dosanjh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Kuldip Singh Dhir&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Lakhvir Lezia&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Mohan Singh Rattan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Nahar Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Rajiv Mahajan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Satinderjit Kuar'buttar'&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Snadeep Kumar&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Surjeet Kaur Sohal&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Dr.Swaranjit Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gagandeep Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurcharan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurcharan Singh Gill&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurdial Singh Raushan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurlzar Singh Sandhu&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurpreet Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurrdial Raushan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurudas Singh Nirman&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Gurumrl Sidhu&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Hardeep Kulam&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Hardeep Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harinderjit Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harman Haas&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harmandeep Kaur(Dr.)&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harminder Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harnam Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Harparveen Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Jagjiwan Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Jasbir Singh Dosanjh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Jasbir Singh Ghulal&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Jeet Harjeet&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Kamaljit Kanwar&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Kamaljit Singh Banwait&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Kewal Dhaliwal&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Kuljeet Maan&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Malkit Jaura&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Manjit Btalvi&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Manjit Kaur&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Manmohan Singh Daon&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Mohanjit&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Muhammad Imtiaz&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Narinder Pal Singh Komal&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Narinder Singh Kapoor&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Noor Muhammad 'Noor'&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Noor Muhammad Noor&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Parmpal Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Parshotam Singh Lalli&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Pawan Gillanwala&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Prof.Achhru Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Rattan Reehal ( Dr)&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Richard Bach&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Ripudaman Singh Roop&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Sankokh Singh Ambalvi&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Sarabjeet Singh Virdi&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Satpal Kaur Sodhi&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Sharan Arora&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Simranjit Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Sulakhan Sarhaddi&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Supinder Singh Rana&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Surinder Pal Singh&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Surjit Patar&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Suvaran Singh Virk&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Tarsem S Ghuman&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Vishav bandhu&submit_search=Search&author=yes",
            "http://www.unistarbooks.com/search?controller=search&search_query=Zahida Hina&submit_search=Search&author=yes",};
        
        for(String link:str)
        {
            scrapeLinks(link);
        }
    }

}
