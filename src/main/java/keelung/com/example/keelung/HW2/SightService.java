package keelung.com.example.keelung.HW2;

import keelung.com.example.keelung.HW1.KeelungSightsCrawler;
import keelung.com.example.keelung.HW1.Sight;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SightService {
    private final KeelungSightsCrawler crawler;

    public SightService(KeelungSightsCrawler crawler){
        this.crawler = crawler;
    }

    public List<Sight> getSightsByZone(String zone){
        return crawler.getItems(zone);
    }
}
