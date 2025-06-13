package keelung.com.example.keelung.homework1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeelungSightsCrawler {
    //總覽網址
    private static final String BASE_URL = "https://www.travelking.com.tw";
    private final Map<String, List<Sight>> sightsMap = new HashMap<>();

    public List<Sight> crawlAllSights() {
        List<Sight> allSights = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(BASE_URL + "/tourguide/taiwan/keelungcity/").timeout(10000).get();

            //找到並取得"基隆景點列表"區塊
            Element wrapper = doc.select("#guide-point .box").first();
            Elements zones = wrapper.select("h4");
            Elements sights = wrapper.select("ul");

            //依序取得每個區的所有景點資料，並將其儲存到 sightsMap
            for (int i = 0; i < zones.size(); i++){
                String zone = zones.get(i).text().trim();
                Elements zoneSights = sights.get(i+1).select("li a");
                List<Sight> sightsList = new ArrayList<>();

                if (zoneSights != null){
                    //取得目前區下的每個景點，並深入該景點頁面取得更多景點資訊
                    for (Element zoneSight: zoneSights){
                        String sightName = zoneSight.text().trim();
                        String sightURL = BASE_URL + zoneSight.attr("href"); // 景點的 URL

                        try {
                            //抓進景點頁面(第二層)
                            Document sightDoc = Jsoup.connect(sightURL).get();
                            String category = sightDoc.select(".point_type span[property=rdfs:label]").first().text();
                            Element sightWrapper = sightDoc.select("#point_area").first();
                            String photoURL = sightWrapper.select("meta[itemprop=image]").attr("content");
                            String description = sightWrapper.select("meta[itemprop=description]").attr("content");
                            String address = sightWrapper.select("meta[itemprop=address]").attr("content");

                            if (photoURL == null || photoURL.isEmpty()){
                                photoURL = "/notFound.png";
                            }

                            //建立 Sight
                            Sight s = new Sight();
                            s.setZone(zone);
                            s.setSightName(sightName);
                            s.setCategory(category);
                            s.setPhotoURL(photoURL);
                            s.setDescription(description);
                            s.setAddress(address);

                            sightsList.add(s);
                        }catch (IOException e){
                            System.err.println(sightName + " 景點頁面爬取失敗");
                            System.err.println("連線失敗，請檢查網路或網址正確性！");
                            e.printStackTrace();
                        }catch (Exception e){
                            System.err.println(sightName + " 景點頁面爬取失敗");
                            System.out.println("其他錯誤：" + e);
                            e.printStackTrace();
                        }
                    }
                }

                sightsMap.put(zone, sightsList);
                allSights.addAll(sightsList);
            }

            System.out.println("所有景點及頁面爬取成功");
        } catch (IOException e){
            System.err.println("主頁爬取失敗");
            System.err.println("連線失敗，請檢查網路或網址正確性！");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("主頁爬取失敗");
            System.out.println("其他錯誤：" + e);
            e.printStackTrace();
        }

        return allSights;
    }

    public List<Sight> getSightsByZone(String targetZone){
        //如果沒有輸入，直接返回
        if (targetZone == null || targetZone.isEmpty()) {
            System.out.println("該區不存在");
            return new ArrayList<>();
        }
        //如果輸入沒有加"區"，自動加上
        if (!targetZone.endsWith("區")){
            targetZone += "區";
        }

        return sightsMap.getOrDefault(targetZone, new ArrayList<>());
    }
}