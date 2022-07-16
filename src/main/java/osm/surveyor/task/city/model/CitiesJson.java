package osm.surveyor.task.city.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CitiesJson {
	
	private String site;
	
	private List<CityJson> list = new ArrayList<>();
	
}
