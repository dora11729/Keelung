package keelung.com.example.keelung.homework3;

import keelung.com.example.keelung.homework2.SightService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SightLoader implements ApplicationRunner {
    private final SightService sightService;

    public SightLoader(SightService sightService){
        this.sightService = sightService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception{
        sightService.crawlAndSaveSights();
    }
}
