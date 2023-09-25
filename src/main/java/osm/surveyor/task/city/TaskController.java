package osm.surveyor.task.city;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.city.model.City;
import osm.surveyor.task.city.model.Citymesh;
import osm.surveyor.task.city.model.CitymeshPK;
import osm.surveyor.task.city.model.Operation;
import osm.surveyor.task.city.model.Status;
import osm.surveyor.task.city.model.TaskEntity;

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
	public String showList(Model model,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode)
	{
        City city = cityRepository.findByCitycode(citycode);
		model.addAttribute("citycode", citycode);
		model.addAttribute("cityname", city.getCityname());
		model.addAttribute("meshcode", meshcode);
		model.addAttribute("site", city.getSite());
		
		Citymesh mesh = meshRepository.findOne(citycode,meshcode);
        model.addAttribute("mesh", mesh);
        
        String meshstr = "{}";
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
			meshstr = objectMapper.writeValueAsString(mesh);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        model.addAttribute("meshstr", meshstr);
        
        model.addAttribute("mesh", mesh);
        
        String citystr = "{}";
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
			citystr = objectMapper.writeValueAsString(city);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        model.addAttribute("citystr", citystr);
        
		List<TaskEntity> tasks = taskRepository.serchByMesh(citycode, meshcode);
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
	@GetMapping("/task/add")
	public String addTask(Model model,
			@RequestParam(name="op") String op,
			@RequestParam(name="citycode") String citycode,
			@RequestParam(name="meshcode") String meshcode)
	{
		String next = "task";
		Operation operation = Operation.NOP;
		Status nextStatus = Status.PREPARATION;
		if (op.equals(Operation.RESERVE.toString())) {
			model.addAttribute("command", "編集者登録");
			operation = Operation.RESERVE;
			nextStatus = Status.RESERVED;
		}
		else if (op.equals(Operation.CANCEL.toString())) {
			model.addAttribute("command", "編集取消");
			operation = Operation.CANCEL;
			nextStatus = Status.ACCEPTING;
		}
		else if (op.equals(Operation.OK.toString())) {
			model.addAttribute("command", "編集完了");
			operation = Operation.OK;
			nextStatus = Status.OK;
			next = "task_done";
		}
		else if (op.equals(Operation.NG.toString())) {
			model.addAttribute("command", "編集(NG)");
			operation = Operation.NG;
			nextStatus = Status.ACCEPTING;
		}

        City city = cityRepository.getById(citycode);
		model.addAttribute("citycode", citycode);
		model.addAttribute("cityname", city.getCityname());
		model.addAttribute("meshcode", meshcode);
		
		CitymeshPK pk = new CitymeshPK();
		pk.setCitycode(citycode);
		pk.setMeshcode(meshcode);
		Citymesh mesh = meshRepository.getById(pk);
		
		TaskEntity pre = service.getTaskByMesh(citycode, meshcode);
		if (pre != null) {
			pre.setOperation(operation);
			pre.setStatus(nextStatus);
			model.addAttribute("task", pre);
			return next;
		}
		else {
			// 既存Taskが無い場合は生成する
			String uuid = UUID.randomUUID().toString();
			TaskEntity task = new TaskEntity();
			task.setCurrentId(uuid);
			task.setPreId(uuid);
			task.setCitycode(citycode);
			task.setMeshcode(meshcode);
			task.setMesh(mesh);
			task.setStatus(nextStatus);
			task.setUsername("");
			task.setOperation(operation);
			model.addAttribute("task", task);
			return next;
		}
	}
	
	@PostMapping("/task/process")
	public String process(@Validated @ModelAttribute TaskEntity task,
			BindingResult result)
	{
		if (result.hasErrors()) {
			// エラーがある場合
			return nextPage(task);
		}
		service.add(task);
		
		return "redirect:/tasks?citycode="+ task.getCitycode() +"&meshcode="+ task.getMeshcode();
	}
	
	@GetMapping("/admin")
	public String admin()
	{
		return "admin";
	}

	@PostMapping("/admin/download")
    public String download(HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream();) {
        	List<TaskEntity> list = taskRepository.findAll();
        	StringBuffer sb = new StringBuffer();
    		boolean c1 = false;
        	sb.append("[");
        	sb.append(System.lineSeparator());
        	for (TaskEntity task : list) {
    			if (c1) {
    				sb.append(",");
    			}
    			else {
    				c1 = true;
    			}
        		sb.append(task.toString());
            	sb.append(System.lineSeparator());
        	}
        	sb.append("]");
            byte[] fb1 = String.valueOf(sb).getBytes();

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ "task-bldg.json");
            response.setContentLength(fb1.length);
            os.write(fb1);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
	
	private String exceptionHandler(TaskEntity task, Model model) {
		if (task == null) {
			return "error";
		}
		
		if (task.getOperation() == Operation.RESERVE) {
			model.addAttribute("command", "タスク予約");
		}
		else if (task.getOperation() == Operation.CANCEL) {
			model.addAttribute("command", "タスク予約取消");
		}
		else if (task.getOperation() == Operation.OK) {
			model.addAttribute("command", "編集済み");
		}
		else if (task.getOperation() == Operation.NG) {
			model.addAttribute("command", "編集(NG)");
		}
		model.addAttribute("citycode", task.getCitycode());
		model.addAttribute("meshcode", task.getMeshcode());
		model.addAttribute("task", task);
		
		return nextPage(task);
	}
	
	private String nextPage(TaskEntity task) {
		if (task.getOperation() == Operation.OK) {
			return "task_done";
		}
		return "task";
	}
}
