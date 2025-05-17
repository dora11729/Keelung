package keelung.com.example.keelung.HW2;

import jakarta.annotation.PostConstruct;
import keelung.com.example.keelung.HW1.KeelungSightsCrawler;
import keelung.com.example.keelung.HW1.Sight;
import keelung.com.example.keelung.HW3.SightRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SightService {
    private final KeelungSightsCrawler crawler;
    private final SightRepository sightRepository;

    public SightService(KeelungSightsCrawler crawler, SightRepository sightRepository){
        this.crawler = crawler;
        this.sightRepository = sightRepository;
    }

    public void crawlAndSaveSights(){
        if (sightRepository.count() == 0){
            List<Sight> sights = crawler.crawlAllSights();
            sightRepository.saveAll(sights);
            System.out.println("資料初始化完成，共存入 " + sights.size() + " 個景點");
        } else {
            System.out.println("資料庫已有資料，跳過初始化");
        }
    }

    // 從 Crawler 的 HashMap 取
    public List<Sight> getSightsByZone(String zone){
        return crawler.getSightsByZone(zone);
    }

    // 從 Repository 取
    public List<Sight> findSightsFromDB(String zone){
        System.out.println("從資料庫查詢 " + zone);
        return sightRepository.findByZone(zone);
    }
}
