package osm.surveyor.task.city;

import java.io.InputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.CitiesJson;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.CityJson;
import osm.surveyor.task.city.model.Coordinates;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final CityRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		
        // 「src/main/resources/static/city/index.json」を読み込む
        try (InputStream is = new ClassPathResource("static/city/index.json").getInputStream()) {
        	CitiesJson pojo = new ObjectMapper().readValue(is, CitiesJson.class);
        	for (CityJson citiesJson : pojo.getList()) {
        		City city = new City();
        		city.setCitycode(citiesJson.getCode());
        		city.setCityname(citiesJson.getName());
        		city.setFolder(citiesJson.getPath());

        		Coordinates coordinates = citiesJson.toCoordinates();
        		city.setCoordinates(coordinates.getLng(), coordinates.getLat());
        		
        		repository.save(city);
        	}
        }
	}
}