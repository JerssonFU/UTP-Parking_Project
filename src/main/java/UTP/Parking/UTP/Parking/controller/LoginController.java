package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MessageSource messageSource;

    @GetMapping({"/", "/login"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model,
                        Principal principal,
                        RedirectAttributes flash,
                        Locale locale) {

        if (principal != null) {
            flash.addFlashAttribute("info", messageSource.getMessage("text.login.already", null, locale));
            return "redirect:/inicio";
        }

        if (error != null) {
            model.addAttribute("error", messageSource.getMessage("text.login.error", null, locale));
        }

        if (logout != null) {
            model.addAttribute("success", messageSource.getMessage("text.login.logout", null, locale));
        }

        return "login";
    }

    @RequestMapping(value = "/inicio", method = RequestMethod.GET)
    public String inicio(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            model.addAttribute("rol", principal.getRole() != null ? "ROLE_" + principal.getRole().name() : "");
        }
        return "app/inicio";
    }

    @RequestMapping(value = "/error_403", method = RequestMethod.GET)
    public String error403Redirect() {
        return "redirect:/error403";
    }

    @RequestMapping(value = "/error403", method = RequestMethod.GET)
    public String error403() {
        logger.warn("Acceso denegado");
        return "error_403";
    }
}
