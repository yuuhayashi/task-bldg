package osm.surveyor.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.model.City;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	private final CityRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		City city = new City();
		city.setCitycode("01100");
		city.setCityname("北海道 札幌市");
		city.setFolder("01100_sapporo-shi_2020");
		repository.save(city);

		city = new City();
		city.setCitycode("07203");
		city.setCityname("福島県 郡山市");
		city.setFolder("07203_koriyama-shi_2020");
		repository.save(city);
	}

}
