package osm.surveyor.task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.model.City;

@RequiredArgsConstructor
@Controller
public class CityController {
	private final CityRepository repository;
	
	@GetMapping("/")
	public String showList(Model model) {
		model.addAttribute("cities", repository.findAll());
		return "index";
	}
	
	@GetMapping("/add")
	public String addCity(@ModelAttribute City city) {
		return "form";
	}
	
	@PostMapping("/process")
	public String process(@Validated @ModelAttribute City city, BindingResult result) {
		if (result.hasErrors()) {
			return "form";
		}
		repository.save(city);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String editCity(@PathVariable Long id, Model model) {
		model.addAttribute("city", repository.findById(id));
		return "form";
	}

	@GetMapping("/delete/{id}")
	public String deleteCity(@PathVariable Long id) {
		repository.deleteById(id);
		return "redirect:/";
	}
}
