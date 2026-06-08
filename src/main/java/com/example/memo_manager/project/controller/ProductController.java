package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String index(Model map) throws Exception {
        Product productForm = new Product();
        map.addAttribute("productForm", productForm);
        Optional.ofNullable(productForm).orElse(productForm);

        return "product/index";
    }

    @PostMapping("search")
    public String searchProduct(@ModelAttribute Product productForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        session.setAttribute("productForm", productForm);
        PageHelper.startPage(pageNum, pageSize);
        List<Product> searchResult = productService.findSearch(productForm);
        PageInfo<Product> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("productList", searchResult);
        model.addAttribute("productForm", productForm);
        model.addAttribute("pageInfo", pageInfo);
        return "product/index";
    }

    @GetMapping("search")
    public String searchPageProduct(@ModelAttribute Product productForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        productForm = session.getAttribute("productForm") == null ? productForm : (Product) session.getAttribute("productForm");
        PageHelper.startPage(pageNum, pageSize);
        List<Product> searchResult = productService.findSearch(productForm);
        PageInfo<Product> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("productList", searchResult);
        model.addAttribute("productForm", productForm);
        model.addAttribute("pageInfo", pageInfo);
        return "product/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        return "product/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product, Model model, RedirectAttributes redirectAttributes) {
        Integer count = productService.findProductCount(product);
        if(count != null && count > 0) {
            model.addAttribute("message", "该商品已存在");
            model.addAttribute("product", product);
            return "product/form";
        }
        if (product.getProductId() == null) {
            productService.save(product);
            redirectAttributes.addFlashAttribute("messageSuccess", "商品信息登入成功");
        } else {
            productService.update(product);
            redirectAttributes.addFlashAttribute("messageSuccess", "商品信息更新成功");
        }
        return "redirect:/product/search";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/product/search";
    }

}
