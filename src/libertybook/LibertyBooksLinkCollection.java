/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libertybook;

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
public class LibertyBooksLinkCollection {
//

    public static void main(String[] args) {
        crawlCategoryURLs();
    }

    private static void crawlCategoryURLs() {
        String urls[] = {
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_802",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_702",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_918",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_930",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_930_542",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_930_70",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1314",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_876_877",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_876_878",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_876_879",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_888",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_886",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_891",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_92",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_887",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_96",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_884",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_102",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_103",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_107",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_529",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_890",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_109",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_885",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_482",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_119",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_881_120",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_1060",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_1062",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_1063",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_1064",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_941",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_959",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_330_190",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_909",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_910",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_191",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_915",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_914",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_911",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_906_908",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_929",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_923",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_925",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_69",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_919",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_922",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_924",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_917_927",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1289",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1290",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1282",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1288",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1293",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1295",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1285",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1283",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1294",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1286",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1284",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1292",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1281",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1287",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_170_1291",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1145",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_931_934",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_931_936",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_931_1121",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_931_932",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_931_933",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_175",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1012",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1015",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1011",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1010",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1018",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_1020",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_940_393",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1155",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1144",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_926",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1115",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_169_1170",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_303",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_1167",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_211",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_210",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_207",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_206",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_208",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_209",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_212",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_525",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_173_521",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_921",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_101",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1164",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_950",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1153",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_858",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1137",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1138",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1301",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1140",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1141",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1136",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1135",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1142",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1143",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1302",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1103_1303",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_951",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1261",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1162",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1108",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1150",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1149",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1280",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_273",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_186",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1050",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_292",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_184_1105",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_672",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_185_982",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_185_277",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_185_281",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_185_986",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_185_86",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_981",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_108",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_287",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_979",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_978",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_977",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_980",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_363",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_187_286",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_1013",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_958_971",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_958_81",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_958_970",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_958_969",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_420",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_427",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_414",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_963",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_428",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_438",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_964",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_791",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_416",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_189_782",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_516",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1002",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1005",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_296",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1003",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1007",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1009",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_1004",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_300",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_192",
            "https://www.libertybooks.com/index.php?route=product/category&path=51_163_326",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1323",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1324",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1325",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1326",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1327",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1328",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1329",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1330",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1331",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1332",
            "https://www.libertybooks.com/index.php?route=product/category&path=1322_1333",
            "https://www.libertybooks.com/index.php?route=product/category&path=1102",
            "https://www.libertybooks.com/Ramdan-Books-On-Sale/Ramada-Self-Hep-Books",
            "https://www.libertybooks.com/Books-of-the-Month-May",
            "https://www.libertybooks.com/Books-to-Screen",
            "https://www.libertybooks.com/Corona-Virus-Recommended-Books",
            "https://www.libertybooks.com/index.php?route=product/category&path=1122",
            "https://www.libertybooks.com/Best-Holiday-Reads",
            "https://www.libertybooks.com/index.php?route=product/category&path=1173",
            "https://www.libertybooks.com/index.php?route=product/category&path=1168_1306",
            "https://www.libertybooks.com/index.php?route=product/category&path=1168_1310",
            "https://www.libertybooks.com/index.php?route=product/category&path=1168_1311",
            "https://www.libertybooks.com/Mothers-Day-Special-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Beyond-Mom-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Coffee-Table",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Cooking-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Fiction-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Poetry-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Self-Help-Books",
            "https://www.libertybooks.com/Mothers-Day-Special-Books/Urdu-Novels",
            "https://www.libertybooks.com/index.php?route=product/category&path=1230",
            "https://www.libertybooks.com/Play-At-Home",
            "https://www.libertybooks.com/Ramdan-Books-On-Sale",
            "https://www.libertybooks.com/Ramdan-Books-On-Sale/Ramadan-Cooking-Books",
            "https://www.libertybooks.com/Ramdan-Books-On-Sale/Ramadan-Islamic-Books",
            "https://www.libertybooks.com/Ramdan-Books-On-Sale/Ramadan-Religion-And-Spirituality-Books",};

        for (String u : urls) {
            startLinkScraping(u);
        }
    }

    private static void startLinkScraping(String url) {
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
                /* if (!doc.getElementsByClass("pagination").isEmpty()
                        && !doc.getElementsByClass("pagination").first().getElementsContainingOwnText(">").isEmpty()) {
                    hasNextPage = true;
                    url = doc.getElementsByClass("pagination").first().getElementsContainingOwnText(">").first().attr("href");
                }*/
            } catch (IOException ex) {
                Logger.getLogger(LibertyBooksLinkCollection.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hasNextPage);
    }

}
