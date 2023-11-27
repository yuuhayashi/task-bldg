package osm.surveyor.task.city;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.CitiesJson;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.CityJson;
import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.city.model.TaskEntity;
import osm.surveyor.task.util.Geojson;
import osm.surveyor.task.util.JsonFeature;
import osm.surveyor.task.util.JsonGeometryLine;
import osm.surveyor.task.util.JsonGeometryPoint;
import osm.surveyor.task.util.JsonProperties;
import osm.surveyor.task.util.Point;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final CityRepository cityRepository;
	private final CitymeshRepository meshRepository;
	private final TaskService taskService;
	private final CityService cityService;

	String url = "http://surveyor.mydns.jp/osm-data";
	
	@Override
	public void run(String... args) throws Exception {
        // 「src/main/resources/static/city/index.json」を読み込む
		String body = httpGet(url + "/index.json");
    	
    	CitiesJson pojo = new ObjectMapper().readValue(body, CitiesJson.class);
    	String site = pojo.getSite();
    	for (CityJson citiesJson : pojo.getList()) {
    		City city = new City();
    		city.setSite(site);
    		city.setCitycode(citiesJson.getCode());
    		city.setCityname(citiesJson.getName());
    		city.setFolder(citiesJson.getPath());
    		Status status = citiesJson.getStatus();
    		if (status != null) {
    			city.setStatus(status);
    		}

    		Point coordinates = citiesJson.toCoordinates();
    		city.setLng(coordinates.getLng());
    		city.setLat(coordinates.getLat());

    		cityRepository.save(city);
    		storeTask(city);
    		cityService.updateStatus(city.getCitycode());
        }
	}
	
	private String httpGet(String url) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println(String.format("INFO: httpGet(%s)", url));
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		HttpStatus httpStatus = response.getStatusCode();
		if (httpStatus.isError()) {
			System.out.println("ERROE: Can not access '"+ url +"'");
			throw new Exception("ERROE: Can not access '"+ url +"'");
		}
		String body = response.getBody();
		return body;
	}
	
	private void storeTask(City city) throws Exception {
		String folder = city.getFolder();
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode node = mapper.readTree(httpGet(url + "/" + folder + "/bldg/index.geojson"));
    	Geojson geojson = new Geojson();
    	geojson.parse(node);
    	
    	List<JsonFeature> features = geojson.getFeatures();
    	for (JsonFeature f : features) {
        	JsonProperties prop = f.getProperties();
        	if (prop != null) {
        		String meshcode = prop.getId();
        		if (meshcode != null) {
        			// メッシュの中心点
    	    		JsonGeometryPoint geometryPoint = f.getGeometryPoint();
    	    		if (geometryPoint != null) {
                    	Citymesh mesh = new Citymesh();
                    	mesh.setCitycode(city.getCitycode());
            			mesh.setMeshcode(meshcode);
            			mesh.setVersion(prop.getVersion());
            			mesh.setSurveyYear(prop.getSurveyYear());
            			mesh.setPath(prop.getPath());
            			mesh.setPoint(geometryPoint.getPoint());
            			mesh.setCity(city);
            			
            			TaskEntity task = (TaskEntity) taskService.getTaskByMesh(city.getCitycode(), meshcode);
            			if (task == null) {
                    		Status status = city.getStatus();
                    		if (status != null) {
                    			mesh.setStatus(status);
                    		}
            			}
            			else {
            				mesh.setStatus(task.getStatus());
            				mesh.setUsername(task.getUsername());
            			}
            			meshRepository.save(mesh);
    	    		}
    	    		
    	    		// メッシュの範囲
    	    		// [制限事項] 'index.geojson' 内の配列は必ず、LINE＿STRINGフィーチャの前にPOINTフィーチャが存在していなければならない
    				JsonGeometryLine geometryLine = f.getGeometryLine();
    	    		if (geometryLine != null) {
                    	Citymesh mesh = new Citymesh();
                    	mesh.setCitycode(city.getCitycode());
            			mesh.setMeshcode(meshcode);
            			mesh.setLine(geometryLine);
            			mesh.setPoint(geometryLine.getCenter());
            			mesh.setCity(city);
            			
            			// [制限事項]
        				Citymesh meshDb = (Citymesh) meshRepository.findOne(city.getCitycode(), meshcode);
        				if (meshDb != null) {
                			mesh.setVersion(meshDb.getVersion());
                			mesh.setSurveyYear(meshDb.getSurveyYear());
                			mesh.setPath(meshDb.getPath());
                			mesh.setLng(meshDb.getLng());
                			mesh.setLat(meshDb.getLat());
            				mesh.setStatus(meshDb.getStatus());
            				mesh.setUsername(meshDb.getUsername());
                			mesh.setStatus(meshDb.getStatus());
        				}
            			meshRepository.save(mesh);
    	    		}
        		}
        	}
    	}
	}
}