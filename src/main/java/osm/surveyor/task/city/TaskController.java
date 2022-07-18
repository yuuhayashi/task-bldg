package osm.surveyor.task.city;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.Task;

@RequiredArgsConstructor
@Controller
public class TaskController {
	private final TaskRepository repository;

	@GetMapping("/task/{citycode}")
	public String showList(@PathVariable String citycode, Model model) {
		List<Task> tasks = repository.findByCitycode(citycode);
		model.addAttribute("tasks", tasks);
		return "tasks";
	}
}
