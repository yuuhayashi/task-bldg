package osm.surveyor.task.plateau;

import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PlateauController {
	@Value("${task-bldg.osm-data.url}")
	private String url;		// task-bldg.osm-data.url=http://surveyor.mydns.jp/osm-data

	@Value("${task-bldg.plateau.dir}")
	private String dir;		// task-bldg.plateau.dir=plateau-data

	@GetMapping(value = "/plateau/{citycode}}")
	public String getPlateau(@PathVariable("citycode") String citycode) throws Exception {
		try {
			Path base = Path.of(dir);
			if (Files.exists(base)) {
				Files.createDirectory(base);
			}
			Path city = Path.of(dir, citycode);
			if (Files.exists(city)) {
				Files.createDirectory(city);
			}
			return "OK";
		}
		catch (Exception e) {
			return e.toString();
		}

		/*
		Path path = Path.of(dir.toString(), fileName);
		PathResource resource = new PathResource(path);
		
		try {
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
		catch (Exception e) {
			return e.toString();
		}
		*/
	}
}