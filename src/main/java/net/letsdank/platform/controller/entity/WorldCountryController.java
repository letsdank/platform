package net.letsdank.platform.controller.entity;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.common.WorldCountry;
import net.letsdank.platform.model.common.PageInfo;
import net.letsdank.platform.repository.common.WorldCountryRepository;
import net.letsdank.platform.service.common.WorldCountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/entity/world-country")
@AllArgsConstructor
public class WorldCountryController {
    private final WorldCountryService service;
    private final WorldCountryRepository repository;

    @GetMapping
    public String listPage(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable paging = PageRequest.of(page - 1, size).withSort(Sort.Direction.ASC, "name");
            Page<WorldCountry> pPage = repository.findAll(paging);
            model.addAttribute("page", new PageInfo<>(pPage));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "app/common/entity/world-country/index";
    }

    @GetMapping("/{id}")
    public String itemModal(Model model, @PathVariable Long id) {
        Optional<WorldCountry> country = repository.findById(id);
        if (country.isEmpty()) {
            return "redirect:/entity/world-country"; // TODO: Вывести ошибку
        }
        model.addAttribute("item", country.get());
        return "app/common/entity/world-country/item";
    }
}
