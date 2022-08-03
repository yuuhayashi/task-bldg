package osm.surveyor.task.city;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Operation;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.city.model.Task;

@RequiredArgsConstructor
@Controller
public class TaskController {
	private final CityRepository cityRepository;
	private final CitymeshRepository meshRepository;
	private final TaskRepository taskRepository;
	
	@Autowired
	private TaskService service;
	
	/**
	 * ログインユーザーが関係しているTASKリスト
	 * @param model
	 * @return
	 */
	@GetMapping("/tasks")
	public String showList(@AuthenticationPrincipal UserDetails user, Model model,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode)
	{
		// ログイン名を取得
		String loginName = "";
    	if (user != null) {
    		loginName = user.getUsername();
    	}
        model.addAttribute("username", loginName);

        City city = cityRepository.getById(citycode);
		model.addAttribute("citycode", citycode);
		model.addAttribute("cityname", city.getCityname());
		model.addAttribute("meshcode", meshcode);
		
		CitymeshPK pk = new CitymeshPK();
		pk.setCitycode(citycode);
		pk.setMeshcode(meshcode);
		Citymesh mesh = meshRepository.getById(pk);
        model.addAttribute("mesh", mesh);
        
		List<Task> tasks = taskRepository.serchByMesh(citycode, meshcode);
		model.addAttribute("tasks", tasks);
		return "tasks";
	}
	
	/**
	 * 「予約登録」
	 * @param user
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@GetMapping("/task/reserve")
	public String reserve(@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode) 
	{
		// ログイン名を取得
		String loginName = "";
    	if (user != null) {
    		loginName = user.getUsername();
    	}
        model.addAttribute("username", loginName);
        
        City city = cityRepository.getById(citycode);
		model.addAttribute("citycode", citycode);
		model.addAttribute("cityname", city.getCityname());
		model.addAttribute("meshcode", meshcode);
		
		CitymeshPK pk = new CitymeshPK();
		pk.setCitycode(citycode);
		pk.setMeshcode(meshcode);
		Citymesh mesh = meshRepository.getById(pk);
		
		Task pre = service.getTaskByMesh(citycode, meshcode);
		if (pre != null) {
			model.addAttribute("task", pre);
			return "task";
		}
		else {
			// Taskが無い場合は生成する
			String uuid = UUID.randomUUID().toString();
			Task task = new Task();
			task.setCurrentId(uuid);
			task.setPreId(uuid);
			task.setCitycode(citycode);
			task.setMeshcode(meshcode);
			task.setMesh(mesh);
			task.setStatus(Status.PREPARATION);
			task.setUsername(loginName);
			task.setOperation(Operation.RESERVE);
			model.addAttribute("task", task);
			return "task";
		}
	}
	
	@GetMapping("/task/add")
	public String addTask(
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode,
			@ModelAttribute Task task)
	{
		return "task";
	}
	
	@PostMapping("/task/process")
	public String process(@AuthenticationPrincipal UserDetails user, 
			@Validated @ModelAttribute Task task,
			BindingResult result)
	{
		if (result.hasErrors()) {
			// エラーがある場合
			return "task";
		}
		service.add(task);
		
		return "redirect:/tasks?citycode="+ task.getCitycode() +"&meshcode="+ task.getMeshcode();
	}
}
