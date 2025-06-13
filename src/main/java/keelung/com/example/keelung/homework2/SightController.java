package keelung.com.example.keelung.homework2;

import keelung.com.example.keelung.homework1.Sight;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/SightAPI")
public class SightController {
    private final SightService sightService;

    public SightController(SightService sightService){
        this.sightService = sightService;
    }

    /*
    @GetMapping
    public List<Sight> getSights(
            @RequestParam String zone
    ){
        return sightService.getSightsByZone(zone);
    }
     */

    @GetMapping
    public List<Sight> getSightsFromDB(
            @RequestParam String zone
    ){
        return sightService.findSightsFromDB(zone);
    }
}