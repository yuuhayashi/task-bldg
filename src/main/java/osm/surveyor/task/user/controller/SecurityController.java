package osm.surveyor.task.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String success() {
        return "login";
    }

    @GetMapping("/")
    public String showList(Authentication loginUser, Model model) {
    	if (loginUser == null) {
            model.addAttribute("username", "");
            model.addAttribute("role", "");
    	}
    	else {
            model.addAttribute("username", loginUser.getName());
            model.addAttribute("role", loginUser.getAuthorities());
    	}
        return "user";
    }
}
