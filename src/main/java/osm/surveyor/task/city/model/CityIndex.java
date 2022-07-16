package osm.surveyor.task.city.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityIndex {

	private String site;
	
	private List<City> list;

}
