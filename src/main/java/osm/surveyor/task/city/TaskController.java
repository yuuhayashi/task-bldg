package osm.surveyor.task.city;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
		model.addAttribute("command", "タスク登録");

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
			pre.setOperation(Operation.RESERVE);
			pre.setUsername(loginName);
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
	
	/**
	 * 「タスク登録取り消し」
	 * @param user
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@GetMapping("/task/cancel")
	public String cancel(@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode) 
	{
		model.addAttribute("command", "タスク登録取消");

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
			pre.setOperation(Operation.CANCEL);
			pre.setUsername(loginName);
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
			task.setStatus(Status.ACCEPTING);
			task.setUsername(loginName);
			task.setOperation(Operation.CANCEL);
			model.addAttribute("task", task);
			
			return "task";
		}
	}

	/**
	 * 「作業完了」
	 * @param user
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@GetMapping("/task/done")
	public String done(@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode) 
	{
		model.addAttribute("command", "作業終了");

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
			pre.setOperation(Operation.DONE);
			pre.setUsername(loginName);
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
			task.setStatus(Status.IMPORTED);
			task.setUsername(loginName);
			task.setOperation(Operation.DONE);
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
		service.add(task, user);
		
		return "redirect:/tasks?citycode="+ task.getCitycode() +"&meshcode="+ task.getMeshcode();
	}
	
	/**
	 * 406 Not Acceptable
	 * "ACCEPTIONGではないため予約できません"
	 * 
	 * @param e
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@ExceptionHandler(NotAcceptableException.class)
	public String notAcceptableExceptionHandler(NotAcceptableException e, Model model) 
	{
		model.addAttribute("error", "406 Not Acceptable");
		model.addAttribute("message", e.toString());
		model.addAttribute("status", HttpStatus.NOT_ACCEPTABLE);
		
		Task task = e.getTask();
		if (task == null) {
			return "error";
		}
		if (task.getOperation() == Operation.RESERVE) {
			model.addAttribute("command", "タスク登録");
		}
		else if (task.getOperation() == Operation.CANCEL) {
			model.addAttribute("command", "タスク登録取消");
		}
		else if (task.getOperation() == Operation.DONE) {
			model.addAttribute("command", "作業完了");
		}
		model.addAttribute("citycode", task.getCitycode());
		model.addAttribute("meshcode", task.getMeshcode());
		model.addAttribute("task", task);
		return "task";
	}

	/**
	 * 409 Conflict
	 * "タスクが変更されたため更新できません"
	 * 
	 * @param e
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@ExceptionHandler(ConflictException.class)
	public String conflictExceptionHandler(ConflictException e, Model model) 
	{
		model.addAttribute("error", "409 Conflict");
		model.addAttribute("message", e.toString());
		model.addAttribute("status", HttpStatus.CONFLICT);
		
		Task task = e.getTask();
		if (task == null) {
			return "error";
		}
		
		if (task.getOperation() == Operation.RESERVE) {
			model.addAttribute("command", "タスク登録");
		}
		else if (task.getOperation() == Operation.CANCEL) {
			model.addAttribute("command", "タスク登録取消");
		}
		else if (task.getOperation() == Operation.DONE) {
			model.addAttribute("command", "作業完了");
		}
		model.addAttribute("citycode", task.getCitycode());
		model.addAttribute("meshcode", task.getMeshcode());
		model.addAttribute("task", task);
		return "task";
	}
}
