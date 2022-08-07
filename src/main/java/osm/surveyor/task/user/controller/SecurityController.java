package osm.surveyor.task.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import osm.surveyor.task.user.model.SiteUser;
import osm.surveyor.task.user.repository.SiteUserRepository;
import osm.surveyor.task.util.Role;

@RequiredArgsConstructor
@Controller
public class SecurityController {

    private final SiteUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/task-bldg/login")
    public String success() {
        return "login";
    }

    @GetMapping("/task-bldg/")
    public String showList(Authentication loginUser, Model model) {
    	if (loginUser == null) {
            model.addAttribute("username", "");
            model.addAttribute("role", "");
    	}
    	else {
            model.addAttribute("username", loginUser.getName());
            model.addAttribute("role", loginUser.getAuthorities());
    	}
		return "redirect:/task-bldg/city";
    }

    @GetMapping("/task-bldg/admin/list")
    public String showAdminList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "list";
    }

    @GetMapping("/task-bldg/register")
    public String register(@ModelAttribute("user") SiteUser user) {
        return "register";
    }

    @PostMapping("/task-bldg/register")
    public String process(@Validated @ModelAttribute("user") SiteUser user, BindingResult result) {
    	if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.isAdmin()) {
            user.setRole(Role.ADMIN.name());
        } else {
            user.setRole(Role.USER.name());
        }
        userRepository.save(user);

        return "redirect:/task-bldg/login?register";
    }
}
