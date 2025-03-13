package keelung.com.example.keelung.HW1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeelungSightsCrawler {
    //總覽網址
    private static final String BASE_URL = "https://www.travelking.com.tw";

    public List<Sight> getItems(String targetZone) {
        List<Sight> sightsList = new ArrayList<>();

        //如果沒有輸入，直接返回
        if (targetZone == null || targetZone.isEmpty()) {
            System.out.println("該區不存在");
            return sightsList;
        }
        //如果輸入沒有加"區"，自動加上
        if (!targetZone.endsWith("區")){
            targetZone += "區";
        }

        try {
            Document doc = Jsoup.connect(BASE_URL + "/tourguide/taiwan/keelungcity/").timeout(10000).get();

            //找到並取得"基隆景點列表"區塊
            Element wrapper = doc.select("#guide-point .box").first();
            Elements zones = wrapper.select("h4");
            Elements sights = wrapper.select("ul");

            //取得目標區每個景點資料，並將其儲存
            for (int i = 0; i < zones.size(); i++){
                String zone = zones.get(i).text().trim();

                //非目標區直接跳過
                if (!zone.equals(targetZone)) {
                    continue;
                }

                Elements zoneSights = sights.get(i+1).select("li a");
                if (zoneSights != null){
                    //已找到目標區，取得其下每個景點，並深入該景點頁面取得更多景點資訊
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