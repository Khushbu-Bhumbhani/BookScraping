/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biomela;

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
public class BioMelaPublishers {

    public static void main(String[] args) {
        String url = "https://boimela.in/";
        try {
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                    .get();
            Element select = doc.getElementById("select-bookx_publisher_id");
            for (Element o : select.getElementsByTag("option")) {
                System.out.println("" + o.text());
            }
        } catch (IOException ex) {
            Logger.getLogger(BioMelaPublishers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
