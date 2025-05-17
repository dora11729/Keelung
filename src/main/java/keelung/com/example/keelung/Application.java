package keelung.com.example.keelung;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "keelung.com.example.keelung.HW3")
public class Application {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		String uri = dotenv.get("MONGODB_URI");
		if (uri != null) {
			System.setProperty("MONGODB_URI", uri);
			System.out.println("✅ MONGODB_URI 載入成功: " + uri);
		} else {
			System.err.println("❌ 沒有從 .env 讀到 MONGODB_URI！");
		}

		SpringApplication.run(Application.class, args);
	}

}
