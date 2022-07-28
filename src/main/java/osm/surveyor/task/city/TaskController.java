package osm.surveyor.task.city;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Task;

@RequiredArgsConstructor
@Controller
public class TaskController {
	private final TaskRepository taskRepository;
	
	/**
	 * ログインユーザーが関係しているTASKリスト
	 * @param model
	 * @return
	 */
	@GetMapping("/tasks")
	public String showList(Model model) {
		model.addAttribute("tasks", taskRepository.findAll());
		return "tasks";
	}
	
	@GetMapping("/task")
	public String showTask(@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode, Model model) {
		CitymeshPK pk = new CitymeshPK();
		pk.setCitycode(citycode);
		pk.setMeshcode(meshcode);
		model.addAttribute("tasks", taskRepository.findById(pk));
		return "tasks";
	}
	
	@GetMapping("/task/add")
	public String addTask(@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode, @ModelAttribute Task task) {
		return "task";
	}
	
	@PostMapping("/task/process")
	public String process(@Validated @ModelAttribute Task task, BindingResult result) {
		if (result.hasErrors()) {
			return "task";
		}

		taskRepository.save(task);
		return "redirect:/tasks";
	}
}
