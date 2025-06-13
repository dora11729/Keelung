package keelung.com.example.keelung.homework2;

import keelung.com.example.keelung.homework1.KeelungSightsCrawler;
import keelung.com.example.keelung.homework1.Sight;
import keelung.com.example.keelung.homework3.SightRepository;
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
        List<Sight> sights = crawler.crawlAllSights();
        sightRepository.deleteAll();
        sightRepository.saveAll(sights);
        System.out.println("資料初始化完成，共存入 " + sights.size() + " 個景點");
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
