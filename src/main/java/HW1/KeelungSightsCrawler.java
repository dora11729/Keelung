package HW1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class KeelungSightsCrawler {
    private static final String BASE_URL = "https://www.travelking.com.tw";

    public List<Sight> getItems(String targetZone) {
        List<Sight> sightsList = new ArrayList<>();

        if (targetZone == null || targetZone.isEmpty()) {
            System.out.println("該區不存在");
            return sightsList;
        }

        try {
            Document doc = Jsoup.connect(BASE_URL + "/tourguide/taiwan/keelungcity/").timeout(10000).get();
            Element wrapper = doc.select("#guide-point .box").first();
            Elements zones = wrapper.select("h4");
            Elements sights = wrapper.select("ul");

            for (int i = 0; i < zones.size(); i++){
                String zone = zones.get(i).text().trim();
                if (!zone.equals(targetZone)) {
                    //System.out.println("pass: " + zone);
                    continue;
                }

                Elements zoneSights = sights.get(i+1).select("li a");
                if (zoneSights != null){
                    for (Element zoneSight: zoneSights){
                        String sightName = zoneSight.text().trim();
                        String sightURL = BASE_URL + zoneSight.attr("href"); // 景點的 URL

                        //抓進景點頁面(第二層)
                        Document sightDoc = Jsoup.connect(sightURL).get();
                        String category = sightDoc.select(".point_type span[property=rdfs:label]").first().text();
                        Element sightWrapper = sightDoc.select("#point_area").first();
                        String photoURL = sightWrapper.select("meta[itemprop=image]").attr("content");
                        String description = sightWrapper.select("meta[itemprop=description]").attr("content");
                        String address = sightWrapper.select("meta[itemprop=address]").attr("content");

                        //建立 Sight
                        Sight s = new Sight();
                        s.setZone(zone);
                        s.setSightName(sightName);
                        s.setCategory(category);
                        s.setPhotoURL(photoURL);
                        s.setDescription(description);
                        s.setAddress(address);

                        sightsList.add(s);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("爬取失敗：" + e.getMessage());
        }
        return sightsList;
    }

    public static void main(String[] args) {
        KeelungSightsCrawler crawler = new KeelungSightsCrawler();
        List<Sight> sights = crawler.getItems("仁愛區");

        for (Sight s : sights) {
            System.out.println(s);
        }
    }
}