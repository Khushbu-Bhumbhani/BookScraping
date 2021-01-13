/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HindiBook;

import connectionManager.MyConnection;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Khushbu
 */
public class HindiBookLinkScraping {
    
    public static void main(String[] args) {
        /*  String[][] str = {
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Bilinguals&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=﻿द्विभाषीय पुस्तकें, Bilinguals Books&rpp=41", "Bilinguals Books"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Children&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=बाल साहित्य, Children Books&rpp=41", "Children Books"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Dictionaries&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=शब्दकोश, Dictionaries&rpp=41", "Dictionaries"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Novel&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=उपन्यास, Novel&rpp=41", "Novel"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Stories&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=कहानियां, Stories&rpp=41", "Stories"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Satire&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=व्यंग, Satire&rpp=41", "Satire"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Poetry&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=कविता, Poetry&rpp=41", "Poetry"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Play&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=नाटक, Play&rpp=41", "Play"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Journalism&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=पत्रकारिता, Journalism&rpp=41", "Journalism"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Translation&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=अनुवाद, Translation&rpp=41", "Translation"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Art and Culture&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=कला और संस्कृति, Art and Culture&rpp=41", "Art and Culture"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Articles&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=लेख, Articles&rpp=41", "Articles"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Autobiography&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=आत्मकथा, Autobiography&rpp=41", "Autobiography"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Biography&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=जीवनी, Biography&rpp=41", "Biography"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Cinema&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=सिनेमा, Cinema&rpp=41", "Cinema"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Collection of Works&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=रचनावली / संचयन, Collection of Works&rpp=41", "Collection of Works"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Computer&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=कंप्यूटर, Computer&rpp=41", "Computer"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Criticism&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=आलोचना, Criticism&rpp=41", "Criticism"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Dalit Sahitya&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=दलित साहित्य, Dalit Sahitya&rpp=41", "Dalit Sahitya"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Education&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=शिक्षा, Education&rpp=41", "Education"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Essays&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=निबंध, Essays&rpp=41", "Essays"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Health&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=स्वास्थ्य, Health&rpp=41", "Health"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=History&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=इतिहास / राजनीति, History/Politics&rpp=41", "History/Politics"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Linguistics&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=भाषा विज्ञान, Linguistics&rpp=41", "Linguistics"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Music&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=संगीत, Music&rpp=41", "Music"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Philosophy Religion&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=धरम दर्शन, Philosophy Religion&rpp=41", "Philosophy Religion"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Reference&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=संदर्भ ग्रंथ, Reference&rpp=41", "Reference"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Science&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=विज्ञान, Science&rpp=41", "Science"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Sociology&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=समाज शास्त्र, Sociology&rpp=41", "Sociology"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Urdu Poetry&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=उर्दू शायरी, Urdu Poetry&rpp=41", "Urdu Poetry"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Misc Subjects&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=विविध साहित्य, Misc Subjects&rpp=41", "Misc Subjects"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Women Study&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=महिला अध्ययन, Women Study&rpp=41", "Women Study"},
            {"http://hindibook.com/index.php?p=sr&Field=subject&String=Travelogue, Reminiscences&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=यात्रा, Travelogue, Reminiscences&rpp=41", "Travelogue, Reminiscences"}};
         */
        String str[][] = {
            /* {"http://www.hindibook.com/index.php?p=sr&Field=language&String=urdu&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=Urdu&rpp=41","Urdu"},
          {"http://www.hindibook.com/index.php?p=sr&Field=category&String=New%20Releases&rpp=41","New Releases"},
          {"http://www.hindibook.com/index.php?p=sr&Field=category&String=Bestsellers&Ds=Bestsellers%20Books&rpp=41","Best sellers"},
          {"http://www.hindibook.com/index.php?p=sr&Field=category&String=Evergreen&Ds=Evergreen%20Books&rpp=41","Evergreen Books"},
          {"http://www.hindibook.com/index.php?p=sr&Field=subject&String=Own%20Publications&rpp=41","Own Publications"},
          {"http://www.hindibook.com/index.php?p=sr&Field=language&String=English&Ds=English%20Books%22&rpp=41","English Books"},*/
            {"https://www.hindibook.com/index.php?p=sr&String=punjabi-books&Field=keywords", "Punjabi Books 1"}
        };
        for (int i = 0; i < str.length; i++) {
            startLinkScraping(str[i][0], str[i][1]);
        }
    }
    
    private static void startLinkScraping(String mainurl, String category) {
        boolean hasNextPage = false;
        int pageStart = 72;
        // String url="http://hindibook.com/index.php?p=sr&Field=subject&String=Bilinguals&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=%EF%BB%BF%E0%A4%A6%E0%A5%8D%E0%A4%B5%E0%A4%BF%E0%A4%AD%E0%A4%BE%E0%A4%B7%E0%A5%80%E0%A4%AF%20%E0%A4%AA%E0%A5%81%E0%A4%B8%E0%A5%8D%E0%A4%A4%E0%A4%95%E0%A5%87%E0%A4%82,%20Bilinguals%20Books";
        //  String mainUrl = "http://hindibook.com/index.php?p=sr&Field=subject&String=Children&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=%E0%A4%AC%E0%A4%BE%E0%A4%B2%20%E0%A4%B8%E0%A4%BE%E0%A4%B9%E0%A4%BF%E0%A4%A4%E0%A5%8D%E0%A4%AF,%20Children%20Books&rpp=41";
        //   String mainUrl = "http://hindibook.com/index.php?p=sr&Field=subject&String=Art%20and%20Culture&Fln=language&Flv=hindi&Exactly=yes&Match=phrase&arrStr=yes&delStr=,&Ds=%E0%A4%95%E0%A4%B2%E0%A4%BE%20%E0%A4%94%E0%A4%B0%20%E0%A4%B8%E0%A4%82%E0%A4%B8%E0%A5%8D%E0%A4%95%E0%A5%83%E0%A4%A4%E0%A4%BF,%20Art%20and%20Culture&rpp=41";
        String url = mainurl;
        // String rpp="41";
        do {
            try {
                System.out.println("--------------" + url);
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                //rpp=doc.getElementsByAttributeValue("name", "rpp").first().attr("value");
                String insertQuery = "INSERT INTO `books`.`hindi_books_links`\n"
                        + "(\n"
                        + "`book_url`,page_url,category)\n"
                        + "VALUES\n";
                
                for (Element form : doc.getElementsByAttribute("data-equalizer-watch")) {
                    System.out.println(""+form.text());
                    // String detailUrl = "http://hindibook.com/" + form.getElementsByClass("srtitle").first().attr("href");
                    String detailUrl = "http://hindibook.com/" + form.getElementsByTag("a").first().attr("href");
                    //   System.out.println("" + detailUrl);
                    insertQuery = insertQuery + " ('" + detailUrl + "','" + url + "','" + category.replaceAll("'", "''") + "'),";
                }
                MyConnection.getConnection("books");
               MyConnection.insertData(insertQuery.substring(0, insertQuery.length() - 1));
                System.out.println("Inserted!!");
                //pagination
                //if (!doc.getElementsByAttributeValue("name", "mfnextimg").isEmpty()) {
                //https://www.hindibook.com/index.php?p=sr&String=punjabi-books&Field=keywords
                //https://www.hindibook.com/index.php?&p=sr&String=punjabi-books&Field=keywords&perpage=72&startrow=72
                //https://www.hindibook.com/index.php?&p=sr&String=punjabi-books&Field=keywords&perpage=72&startrow=144
                if (!doc.getElementsMatchingOwnText(">").isEmpty()) {
                    hasNextPage = true;
                    url = mainurl + "&perpage=72&startrow=" + pageStart;
                    pageStart = pageStart + 72;
                } else {
                    hasNextPage = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(HindiBookLinkScraping.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
        
    }
    
}
