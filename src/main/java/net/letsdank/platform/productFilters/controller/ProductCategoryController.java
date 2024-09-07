package net.letsdank.platform.productFilters.controller;

import lombok.AllArgsConstructor;
import net.letsdank.platform.productFilters.entity.ProductCategory;
import net.letsdank.platform.productFilters.repository.ProductCategoryRepository;
import net.letsdank.platform.productFilters.service.ProductCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/data-editor/product/category")
@AllArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryService productCategoryService;

    @PostMapping("/upload")
    public String uploadFromFile(@RequestParam MultipartFile file) {
        productCategoryService.upload(file);
        return "redirect:/data-editor/product/category";
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("categories", productCategoryRepository.findAllByParentId(90401L));
        model.addAttribute("category", null);
        return "data-editor/product/category/index";
    }

    @PostMapping()
    public String create(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
        return "redirect:/data-editor/product/category";
    }

    @GetMapping("/{id}")
    public String entry(@PathVariable Long id, Model model) {
        ProductCategory category = productCategoryRepository.findById(id).orElse(null);


        model.addAttribute("categories", productCategoryRepository.findAllByParentId(id));
        model.addAttribute("category", category);

        return "data-editor/product/category/index";
    }
}
