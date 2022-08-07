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
	@GetMapping("/task-bldg/tasks")
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
	 * 「タスク操作」
	 * @param citycode
	 * @param meshcode
	 * @param task
	 * @return
	 */
	@GetMapping("/task-bldg/task/add")
	public String addTask(@AuthenticationPrincipal UserDetails user,
			Model model,
			@RequestParam(name="op") String op,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode)
	{
		String next = "task";
		Operation operation = Operation.NOP;
		Status nextStatus = Status.PREPARATION;
		if (op.equals(Operation.RESERVE.toString())) {
			model.addAttribute("command", "タスク予約");
			operation = Operation.RESERVE;
			nextStatus = Status.RESERVED;
		}
		else if (op.equals(Operation.CANCEL.toString())) {
			model.addAttribute("command", "タスク予約取消");
			operation = Operation.CANCEL;
			nextStatus = Status.ACCEPTING;
		}
		else if (op.equals(Operation.DONE.toString())) {
			model.addAttribute("command", "編集完了");
			operation = Operation.DONE;
			nextStatus = Status.IMPORTED;
			next = "task_done";
		}
		else if (op.equals(Operation.NG.toString())) {
			model.addAttribute("command", "検証(NG)");
			operation = Operation.NG;
			nextStatus = Status.ACCEPTING;
		}
		else if (op.equals(Operation.OK.toString())) {
			model.addAttribute("command", "検証(OK)");
			operation = Operation.OK;
			nextStatus = Status.END;
		}

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
			pre.setOperation(operation);
			if (op.equals(Operation.OK.toString()) || op.equals(Operation.NG.toString())) {
				pre.setValidator(loginName);
			}
			else {
				pre.setUsername(loginName);
			}
			model.addAttribute("task", pre);
			return next;
		}
		else {
			// 既存Taskが無い場合は生成する
			String uuid = UUID.randomUUID().toString();
			Task task = new Task();
			task.setCurrentId(uuid);
			task.setPreId(uuid);
			task.setCitycode(citycode);
			task.setMeshcode(meshcode);
			task.setMesh(mesh);
			task.setStatus(nextStatus);
			task.setUsername(loginName);
			if (op.equals(Operation.OK.toString()) || op.equals(Operation.NG.toString())) {
				task.setValidator(loginName);
			}
			task.setOperation(operation);
			model.addAttribute("task", task);
			return next;
		}
	}
	
	@PostMapping("/task-bldg/task/process")
	public String process(@AuthenticationPrincipal UserDetails user, 
			@Validated @ModelAttribute Task task,
			BindingResult result)
	{
		if (result.hasErrors()) {
			// エラーがある場合
			return nextPage(task);
		}
		service.add(task, user);
		
		return "redirect:/task-bldg/tasks?citycode="+ task.getCitycode() +"&meshcode="+ task.getMeshcode();
	}
	
	/**
	 * 400 Bad Request
	 * 
	 * @param e
	 * @param model
	 * @param citycode
	 * @param meshcode
	 * @return
	 */
	@ExceptionHandler(TaskException.class)
	public String taskExceptionHandler(TaskException e, Model model) 
	{
		model.addAttribute("error", "400 Bad Request");
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.BAD_REQUEST);
		return exceptionHandler(e.getTask(), model);
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
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.NOT_ACCEPTABLE);
		return exceptionHandler(e.getTask(), model);
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
		model.addAttribute("message", e.getMessage());
		model.addAttribute("status", HttpStatus.CONFLICT);
		return exceptionHandler(e.getTask(), model);
	}
	
	private String exceptionHandler(Task task, Model model) {
		if (task == null) {
			return "error";
		}
		
		if (task.getOperation() == Operation.RESERVE) {
			model.addAttribute("command", "タスク予約");
		}
		else if (task.getOperation() == Operation.CANCEL) {
			model.addAttribute("command", "タスク予約取消");
		}
		else if (task.getOperation() == Operation.DONE) {
			model.addAttribute("command", "編集済み");
		}
		else if (task.getOperation() == Operation.NG) {
			model.addAttribute("command", "検証(NG)");
		}
		else if (task.getOperation() == Operation.OK) {
			model.addAttribute("command", "検証(OK)");
		}
		model.addAttribute("citycode", task.getCitycode());
		model.addAttribute("meshcode", task.getMeshcode());
		model.addAttribute("task", task);
		
		return nextPage(task);
	}
	
	private String nextPage(Task task) {
		if (task.getOperation() == Operation.DONE) {
			return "task_done";
		}
		return "task";
	}
}
