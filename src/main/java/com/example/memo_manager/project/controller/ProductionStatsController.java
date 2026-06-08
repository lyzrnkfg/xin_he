package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.ProductionStats;
import com.example.memo_manager.project.service.ProductionStatsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productionStats")
public class ProductionStatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionStatsController.class);

    @Autowired
    private ProductionStatsService productionStatsService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String index(Model map) throws Exception {
        ProductionStats productionStatsForm = new ProductionStats();
        productionStatsForm.setDepList(productionStatsService.findDepList());
        productionStatsForm.setProductList(productionStatsService.findProductList());
        map.addAttribute("productionStatsForm", productionStatsForm);
        Optional.ofNullable(productionStatsForm).orElse(productionStatsForm);

        return "productionStats/index";
    }

    @PostMapping("search")
    public String searchProductionStats(@ModelAttribute ProductionStats productionStatsForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        session.setAttribute("productionStatsForm", productionStatsForm);
        productionStatsForm.setDepList(productionStatsService.findDepList());
        productionStatsForm.setProductList(productionStatsService.findProductList());
        PageHelper.startPage(pageNum, pageSize);
        List<ProductionStats> searchResult = productionStatsService.findSearch(productionStatsForm);
        PageInfo<ProductionStats> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("productionStatsList", searchResult);
        model.addAttribute("productionStatsForm", productionStatsForm);
        model.addAttribute("pageInfo", pageInfo);
        return "productionStats/index";
    }

    @GetMapping("search")
    public String searchPageProductionStats(@ModelAttribute ProductionStats productionStatsForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        productionStatsForm = session.getAttribute("productionStatsForm") == null ? productionStatsForm : (ProductionStats) session.getAttribute("productionStatsForm");
        PageHelper.startPage(pageNum, pageSize);
        List<ProductionStats> searchResult = productionStatsService.findSearch(productionStatsForm);
        PageInfo<ProductionStats> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("productionStatsList", searchResult);
        model.addAttribute("productionStatsForm", productionStatsForm);
        model.addAttribute("pageInfo", pageInfo);
        return "productionStats/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        ProductionStats productionStatsForm = new ProductionStats();
        productionStatsForm.setDepList(productionStatsService.findDepList());
        productionStatsForm.setProductList(productionStatsService.findProductList());
        model.addAttribute("productionStats", productionStatsForm);
        return "productionStats/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProductionStats productionStats, Model model, RedirectAttributes redirectAttributes) {
        List<String> result = productionStatsService.save(productionStats);
        if (result != null && !result.isEmpty()) {
            productionStats.setDepList(productionStatsService.findDepList());
            productionStats.setProductList(productionStatsService.findProductList());
            model.addAttribute("productionStats", productionStats);
            model.addAttribute("messageList", result);
            return "productionStats/form";
        }
        redirectAttributes.addFlashAttribute("messageSuccess", "登入成功");
        return "redirect:/productionStats/search";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productionStatsService.delete(id);
        return "redirect:/productionStats/search";
    }

}
