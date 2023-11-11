package osm.surveyor.task.policy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {
	
	@GetMapping("/policy")
	public String showList(Model model) {
		return "policy";
	}
}
