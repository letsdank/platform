package net.letsdank.platform.controller.entity;

import lombok.AllArgsConstructor;
import net.letsdank.platform.entity.finance.BankClassifier;
import net.letsdank.platform.model.common.PlatformResult;
import net.letsdank.platform.model.common.SuggestResult;
import net.letsdank.platform.repository.finance.BankClassifierRepository;
import net.letsdank.platform.service.finance.BankClassifierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/entity/bank-classifier")
@AllArgsConstructor
public class BankClassifierController {
    private final BankClassifierRepository repository;
    private final BankClassifierService service;

    @GetMapping
    public String listPage(Model model) {
        List<BankClassifier> list = repository.findAllGroups();

        model.addAttribute("items", list);
        return "app/common/entity/bank-classifier/index";
    }

    @GetMapping("/{id}")
    public String itemModal(Model model, @PathVariable Long id) {
        Optional<BankClassifier> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return "redirect:/entity/bank-classifier"; // TODO: Вывести ошибку
        }
        model.addAttribute("item", optional.get());
        return "app/common/entity/bank-classifier/item";
    }

    @PostMapping("/{id}")
    public ResponseEntity<PlatformResult> itemSave(BankClassifier bankClassifier) {
        // TODO: Если нужно вывести ошибки, делаем сервис
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/entity/bank-classifier")
                .build();
    }

    @GetMapping("/create-item")
    public ResponseEntity<PlatformResult> createItemModal() {
        return ResponseEntity.badRequest().body(service.createItem());
    }

    @GetMapping("/create-group")
    public ResponseEntity<PlatformResult> createGroupModal() {
        return ResponseEntity.badRequest().body(service.createGroup());
    }

    @GetMapping("/suggest-group")
    public ResponseEntity<SuggestResult> suggestGroup(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(service.suggestGroup(query));
    }

    @GetMapping("/suggest-item")
    public ResponseEntity<SuggestResult> suggestItem(@RequestParam(name = "query") String query) {
        return ResponseEntity.ok(service.suggestItem(query));
    }
}
