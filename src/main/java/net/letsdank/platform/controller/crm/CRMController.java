package net.letsdank.platform.controller.crm;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/crm")
@AllArgsConstructor
public class CRMController {
    @GetMapping
    public String crmHomePage(Model model) {
        return "app/crm/index";
    }
}
