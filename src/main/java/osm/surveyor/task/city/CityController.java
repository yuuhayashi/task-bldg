package osm.surveyor.task.city;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.City;

@RequiredArgsConstructor
@Controller
public class CityController {
	private final CityRepository repository;
	
	@GetMapping("/city")
	public String showList(Model model) {
		model.addAttribute("cities", repository.findAll());
		return "cities";
	}
	
	@GetMapping("/city/add")
	public String addCity(@ModelAttribute City city) {
		return "form";
	}
	
	@PostMapping("/city/process")
	public String process(@Validated @ModelAttribute City city, BindingResult result) {
		if (result.hasErrors()) {
			return "form";
		}

		repository.save(city);
		return "redirect:/city";
	}

	@GetMapping("/city/edit/{citycode}")
	public String editCity(@PathVariable String citycode, Model model) {
		model.addAttribute("city", repository.findByCitycode(citycode));
		return "form";
	}

	@GetMapping("/city/delete/{citycode}")
	public String deleteCity(@PathVariable String citycode) {
		repository.deleteByCitycode(citycode);
		return "redirect:/city";
	}
}
