package osm.surveyor.task.city;

import java.io.InputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.CityIndex;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final CityRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		
        // 「src/main/resources/static/city/index.json」を読み込む
        try (InputStream is = new ClassPathResource("static/city/index.json").getInputStream()) {
        	
        	/*
        	ObjectMapper mapper = new ObjectMapper();
        	CityIndex index = mapper.readValue(is, CityIndex.class);
        	for (City city : index.getList()) {
        		repository.save(city);
        	}
        	*/
        	
        }
	}

}
