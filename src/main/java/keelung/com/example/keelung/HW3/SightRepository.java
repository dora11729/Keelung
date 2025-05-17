package keelung.com.example.keelung.HW3;

import keelung.com.example.keelung.HW1.Sight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SightRepository extends MongoRepository<Sight, String> {
    List<Sight> findByZone(String zone);
}
