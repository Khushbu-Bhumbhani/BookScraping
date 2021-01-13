/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HindiBook;

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
public class GetCategoryLinks {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("http://hindibook.com/")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            for(Element td:doc.getElementsByClass("leftsubject"))
            {
                String catUrl=td.getElementsByTag("a").first().attr("href");
                String catName=td.getElementsByTag("span").first().text();
                System.out.println("{\"http://hindibook.com/"+catUrl+"\",\""+catName+"\"},");
            }
        } catch (IOException ex) {
            Logger.getLogger(GetCategoryLinks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
