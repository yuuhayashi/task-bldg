package osm.surveyor.task.city;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.CitiesJson;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.CityJson;
import osm.surveyor.task.city.model.Task;
import osm.surveyor.task.util.Geojson;
import osm.surveyor.task.util.JsonFeature;
import osm.surveyor.task.util.JsonGeometryPoint;
import osm.surveyor.task.util.JsonProperties;
import osm.surveyor.task.util.Point;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final CityRepository repository;
	private final TaskRepository taskRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
        // 「src/main/resources/static/city/index.json」を読み込む
        try (InputStream is = new ClassPathResource("static/city/index.json").getInputStream()) {
        	CitiesJson pojo = new ObjectMapper().readValue(is, CitiesJson.class);
        	String site = pojo.getSite();
        	for (CityJson citiesJson : pojo.getList()) {
        		City city = new City();
        		city.setSite(site);
        		city.setCitycode(citiesJson.getCode());
        		city.setCityname(citiesJson.getName());
        		city.setFolder(citiesJson.getPath());

        		Point coordinates = citiesJson.toCoordinates();
        		city.setLng(coordinates.getLng());
        		city.setLat(coordinates.getLat());
        		
        		repository.save(city);
        		storeTask(city);
        	}
        }
	}
	
	private void storeTask(City city) throws IOException {
		String folder = city.getFolder();
		String path = "static/city/"+ folder +"/bldg/index.geojson";
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
        	ObjectMapper mapper = new ObjectMapper();
        	JsonNode node = mapper.readTree(is);
        	Geojson geojson = new Geojson();
        	geojson.parse(node);
        	
        	List<JsonFeature> features = geojson.getFeatures();
        	for (JsonFeature f : features) {
        		JsonGeometryPoint geometryPoint = f.getGeometryPoint();
        		if (geometryPoint != null) {
        			if (geometryPoint.getType().equals("Point")) {
                    	JsonProperties prop = f.getProperties();
                    	if (prop != null) {
                    		String meshcode = prop.getId();
                    		if (meshcode != null) {
                            	Task task = new Task();
                            	task.setCitycode(city.getCitycode());
                    			task.setMeshcode(meshcode);
                    			task.setVersion(prop.getVersion());
                    			task.setPath(prop.getPath());
                    			task.setPoint(geometryPoint.getCoordinates().toString());
                    			taskRepository.save(task);
                    		}
                    	}
        			}
        			
                	// TODO:
        		}
        	}
        	
        	// TODO:
        	System.out.println("["+ path + "] " + geojson.toString());
        }
        
		// TODO:
		
	}
}