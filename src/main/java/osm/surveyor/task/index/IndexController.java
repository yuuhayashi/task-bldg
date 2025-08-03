package osm.surveyor.task.index;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IndexController {

	@GetMapping("/index")
	public String index() {
		String url = "https://surveyor.mydns.jp/osm-data";
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
	}
}