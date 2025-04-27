package osm.surveyor.task.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class GeojsonTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    @DisplayName("'Sapporo'のGeojsonをパース")
	void readSapporo() throws IOException {
		String path = "static/city/01100_sapporo-shi_2020/bldg/index.geojson";
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
        	ObjectMapper mapper = new ObjectMapper();
        	JsonNode node = mapper.readTree(is);
        	assertNotNull(node);
        	Geojson geojson = new Geojson();
        	geojson.parse(node);
        	
        	System.out.println(geojson.toString());
        }
	}

}
