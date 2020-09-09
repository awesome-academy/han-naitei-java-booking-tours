
package com.bookingTour.controller;

import com.bookingTour.model.CategoryInfo;
import com.bookingTour.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
                        Model model, HttpServletRequest request) {
        CategoryInfo categoryAdd = new CategoryInfo();
        CategoryInfo categorySearch = new CategoryInfo();
        categorySearch.setPage(page.orElse(1));
        Page<CategoryInfo> categoryInfos = categoryService.paginate(categorySearch);
        model.addAttribute("category", categoryAdd);
        model.addAttribute("categories", categoryInfos);
        model.addAttribute("categorySearch", categorySearch);
        return "categories/index";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute(name = "categorySearch") CategoryInfo categorySearch, Model model) {
        logger.info("list user page with search");
        Page<CategoryInfo> categories = categoryService.paginate(categorySearch);
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping(value = { "/add" })
    public String add(Locale locale, Model model) {
        model.addAttribute("category", new CategoryInfo());
        return "categories/add";
    }

    @PostMapping(value = "/")
    public String create(@ModelAttribute("category") @Validated CategoryInfo categoryInfo, BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning add category page, validate failed");
            return "categories/create";
        }
        categoryService.addCategory(categoryInfo);
        return "redirect: " + request.getContextPath() + "/categories";
    }

    @GetMapping(value = "{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
    	CategoryInfo category = categoryService.findCategory(id);
    	if (category == null) {
    		model.addAttribute("errorMsg", "Category not found");
			return "templates/error";
		}
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PutMapping(value = "/{id}")
    public String update(@ModelAttribute("category") @Validated CategoryInfo categoryInfo,
                         BindingResult bindingResult, Model model, HttpServletRequest request) throws Exception {
        if (bindingResult.hasErrors()) {
            logger.info("Returning edit category page, validate failed");
            return "categories/edit";
        }
        CategoryInfo category = categoryService.editCategory(categoryInfo);
        return "redirect: " + request.getContextPath() + "/categories/index";
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String destroy(@PathVariable Long id, Model model, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        logger.info("Deleting category: " + id);
        categoryService.deleteCategory(new CategoryInfo(id));
        return "redirect: " + request.getContextPath() + "/categories/index";
    }

}
