/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news.tna_24h;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author XTien
 */
public class NewClass {

    public static void main(String[] args) {
        try {
            Connection connect = Jsoup.connect(String.format("http://m.24h.com.vn/bong-da/cong-phuong-ngo-ngang-khi-gap-cdv-giong-minh-nhu-duc-c48a739582.html"))
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; de-de; rv1.8.1.6) Gecko/20070725 Firefox/25.0")
                    .timeout(2000);
            //System.out.println(html2text(html));
            Document doc = connect.get();
            doc.getElementsByClass("baiviet-bailienquan").remove();
            Elements ps = doc.select("div.newsItem").select("p:not(:has(em)),h2,img.news-image,script");
            PrintWriter writer = new PrintWriter("test.txt", "UTF-8");
            writer.print(doc.html());
            ListIterator<Element> list = ps.listIterator();
            //
            Document doc2 = connect.get();
            doc.getElementsByClass("baiviet-bailienquan").remove();
            Elements ps2 = doc2.select("div.newsItem").select("p:not(:has(em)),h2,img.news-image,script");
            writer.print(doc2.html());
            ListIterator<Element> list2 = ps2.listIterator();
            //
            while (list.hasNext()) {
                Element ele = list.next();
                //System.out.println(ele.toString());
                if (ele.tagName().trim().equals("img")) {
                    System.out.println(ele.toString());
                } else if (ele.html().contains("write_outsite_video_player")) {
                    Pattern p = Pattern.compile("\\'(.*?)\\'");
                    Matcher m = p.matcher(ele.toString());
                    while (m.find()) {
                        if (m.group().contains("http://")) {
                            //video link
                            String[] videos = m.group().replace("'", "").split(",");
                            for (String s : videos) {
                                System.out.println(s.trim());
                            }
                        }
                    }
                } else {
                    if (!ele.text().isEmpty()) {
                        System.out.println(ele.toString());
                    }
                }
            }
            //writer.println(ps.text());
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
