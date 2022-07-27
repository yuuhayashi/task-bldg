package osm.surveyor.task.city;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.Citymesh;

@RequiredArgsConstructor
@Controller
public class CitymeshController {
	private final CitymeshRepository meshRepository;
	private final CityRepository cityRepository;

	@GetMapping("/mesh/{citycode}")
	public String showList(@PathVariable String citycode, Model model) {
		City city = cityRepository.findByCitycode(citycode);
		List<Citymesh> tasks = meshRepository.findByCitycode(citycode);
		model.addAttribute("city", city);
		model.addAttribute("meshes", tasks);
		return "meshes";
	}
}
