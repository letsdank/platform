package net.letsdank.platform.controller.entity;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.common.WorldCountry;
import net.letsdank.platform.model.common.PageInfo;
import net.letsdank.platform.model.common.PlatformResult;
import net.letsdank.platform.repository.common.WorldCountryRepository;
import net.letsdank.platform.service.common.WorldCountryService;
import net.letsdank.platform.utils.MessageService;
import net.letsdank.platform.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            Pageable paging = PageRequest.of(page - 1, size)
                    .withSort(Sort.Direction.ASC, "name");

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

    @PostMapping("/{id}")
    public ResponseEntity<PlatformResult> itemSave(WorldCountry country) {
        PlatformResult result = service.save(country);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/entity/world-country")
                    .build();
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/classifier")
    public String classifierModal(Model model) {
        // TODO: ACL
        model.addAttribute("items", service.getClassifier());
        model.addAttribute("predefined", repository.findPredefinedCodes());

        return "app/common/entity/world-country/classifier";
    }

    @GetMapping("/classifier/{code}")
    public String classifierItemModal(Model model, @PathVariable String code) {
        List<WorldCountry> items = service.getClassifier();
        Optional<WorldCountry> item = items.stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst();
        if (item.isEmpty()) {
            return "redirect:/entity/world-country/classifier"; // TODO: Вывести ошибку
        }

        // Здесь в любом случае выводим модальное окно, только нужно понять
        // данные уже существуют или нет, и выводить от этого разные сообщения
        boolean exists = repository.existsByCode(code);
        String message = StringUtils.substituteParameters(MessageService.getMessage(
                exists ? "common.world-country.classifier.exists" : "common.world-country.classifier.add"),
                item.get().getName());

        model.addAttribute("yes", "/entity/world-country/classifier/" + code + "/add");
        model.addAttribute("message", message);

        return "fragments/modals/dialog-yes-no";
    }

    @GetMapping("/classifier/{code}/add")
    public String addFromClassifier(@PathVariable String code) {
        List<WorldCountry> items = service.getClassifier();
        Optional<WorldCountry> item = items.stream()
                .filter(i -> i.getCode().equals(code))
                .findFirst();
        if (item.isEmpty()) {
            return "redirect:/entity/world-country/classifier"; // TODO: Вывести ошибку
        }

        // Если запись существует, то просто обновляем ее
        Optional<WorldCountry> existing = repository.findByCode(code);
        existing.ifPresent(country -> {
            item.get().setId(country.getId());
            item.get().setPredefined(country.isPredefined());
        });

        repository.save(item.get());
        return "redirect:/entity/world-country";
    }

    @GetMapping("/add")
    public String addModal(Model model) {
        model.addAttribute("item", new WorldCountry());
        return "app/common/entity/world-country/add";
    }
}
