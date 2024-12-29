package osm.surveyor.task.plateau;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.FileSystemUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@WebMvcTest(PlateauController.class)
class PlateauControllerTest {
	
	@Autowired
	private MockMvc mvc;
	private final String rootUrl = "/plateau";
	private static final String dirStr = "plateau-data";		// task-bldg.plateau.dir=plateau-data

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Path dir = Path.of(dirStr);
		FileSystemUtils.deleteRecursively(dir);
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
    @DisplayName("'./plateau/01100' is Sapporo 2020")
	void testGetPlateau() throws IOException {
		String urlPath = "/01100";
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(rootUrl + urlPath))
				.andExpect(status().isOk())
				.andExpect(content().json("OK"))
				.andReturn();
		
		Path base = Path.of(dirStr);
		boolean exists = Files.exists(base);
		assertThat(exists);
	}
}
