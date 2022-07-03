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
import osm.surveyor.task.model.Mapper;

@RequiredArgsConstructor
@Controller
public class MapperController {
	private final MapperRepository repository;
	
	@GetMapping("/")
	public String showList(Model model) {
		model.addAttribute("mappers", repository.findAll());
		return "index";
	}
	
	@GetMapping("/add")
	public String addMapper(@ModelAttribute Mapper mapper) {
		return "form";
	}
	
	@PostMapping("/process")
	public String process(@Validated @ModelAttribute Mapper mapper, BindingResult result) {
		if (result.hasErrors()) {
			return "form";
		}
		repository.save(mapper);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String editMapper(@PathVariable Long id, Model model) {
		model.addAttribute("mapper", repository.findById(id));
		return "form";
	}

	@GetMapping("/delete/{id}")
	public String deleteMapper(@PathVariable Long id) {
		repository.deleteById(id);
		return "redirect:/";
	}
}
