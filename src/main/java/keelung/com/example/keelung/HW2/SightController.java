package keelung.com.example.keelung.HW2;

import keelung.com.example.keelung.HW1.Sight;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/SightAPI")
public class SightController {
    private final SightService sightService;

    public SightController(SightService sightService){
        this.sightService = sightService;
    }

    @GetMapping
    public List<Sight> getSights(
            @RequestParam String zone
    ){
        return sightService.getSightsByZone(zone);
    }
}
