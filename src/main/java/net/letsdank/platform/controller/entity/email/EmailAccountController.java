package net.letsdank.platform.controller.entity.email;


import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.email.EmailAccount;
import net.letsdank.platform.repository.common.EmailAccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/entity/email-account")
@AllArgsConstructor
public class EmailAccountController {
    private final EmailAccountRepository repository;

    @GetMapping
    public String listPage(Model model) {
        List<EmailAccount> list = repository.findAll();

        model.addAttribute("items", list);
        return "app/common/entity/email-account/index";
    }

    @GetMapping("/add")
    public String addModal(Model model) {
        model.addAttribute("item", new EmailAccount());
        return "app/common/entity/email-account/add-helper";
    }
}
