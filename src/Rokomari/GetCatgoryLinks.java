/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rokomari;

import connectionManager.MyConnection;
import connectionManager.Utility;
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
public class GetCatgoryLinks {

    public static void main(String[] args) {
        getMainLinks();
    }

    private static void getMainLinks() {
        //String url = "https://www.rokomari.com/book/authors?ref=mm_va";
        // String url = "https://www.rokomari.com/book/authors?ref=mm_va&page=1127";
        String url = "https://www.rokomari.com/book/categories?ref=mm_va";
        //   String url = "https://www.rokomari.com/book/publishers?ref=mm";

        boolean hasNextPage = false;
        do {
            try {
                System.out.println("Getting..." + url);
                Document doc = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:64.0) Gecko/20100101 Firefox/64.0")
                        .get();
                // Element div=doc.getElementsByClass("authorList").first();
                Element ul = doc.getElementsByClass("categoryList").first();
                /*  for (Element e : doc.getElementsByClass("authorList")) {
              //  for (Element e : doc.getElementsByClass("categoryList")) {
                    if (e.tagName().equalsIgnoreCase("ul")) {
                        ul = e;
                        break;
                    }
                }*/
 /*  String query = "INSERT INTO `books`.`rokomari_categories`\n"
                        + "(`category_name`,\n"
                        + "`category_url`)\n"
                        + "VALUES\n";*/
                if (ul != null) {
                    int i = 1;

                    for (Element li : ul.getElementsByTag("li")) {
                        String name = li.getElementsByTag("h2").first().text();
                        String english_name = li.getElementsByTag("img").first().attr("alt");
                        System.out.println("" + name);
                        String categoryURL = "https://www.rokomari.com" + li.getElementsByTag("a").first().attr("href");
                        System.out.println((i++) + "" + categoryURL);
                        String query = "INSERT INTO `books`.`rokomari_categories_2020`\n"
                                + "(`category_name`,\n"
                                + "`english_name`,\n"
                                + "`category_url`)\n"
                                + "VALUES\n ('" + Utility.prepareString(name) + "','" + Utility.prepareString(english_name) + "','" + Utility.prepareString(categoryURL) + "')";
                        insertData(query);
                        /* query = query + " ('" + Utility.prepareString(name) + "','" +Utility.prepareString( categoryURL) + "'),";*/
                    }
                } else {
                    System.out.println("No UL Tag");
                }
                /* if (ul != null) {
                    insertData(query);
                }*/
                if (!doc.getElementsContainingOwnText("Next").isEmpty()) {
                    hasNextPage = true;
                    url = "https://www.rokomari.com" + doc.getElementsContainingOwnText("Next").first().attr("href");
                } else {
                    hasNextPage = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(GetCatgoryLinks.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }

    private static void insertData(String query) {
        MyConnection.getConnection("books");
        MyConnection.insertData(query);
        System.out.println("Inserted!!");
    }

}
