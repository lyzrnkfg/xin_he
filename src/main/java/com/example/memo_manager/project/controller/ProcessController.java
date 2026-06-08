package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.service.ProcessService;
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
@RequestMapping("/process")
public class ProcessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessService processService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String index(Model map) throws Exception {
        Process processForm = new Process();
        processForm.setProductList(processService.findProductList());
        map.addAttribute("processForm", processForm);
        Optional.ofNullable(processForm).orElse(processForm);

        return "process/index";
    }

    @PostMapping("search")
    public String searchProcess(@ModelAttribute Process processForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        session.setAttribute("processForm", processForm);
        processForm.setProductList(processService.findProductList());
        PageHelper.startPage(pageNum, pageSize);
        List<Process> searchResult = processService.findSearch(processForm);
        PageInfo<Process> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("processList", searchResult);
        model.addAttribute("processForm", processForm);
        model.addAttribute("pageInfo", pageInfo);
        return "process/index";
    }

    @GetMapping("search")
    public String searchPageProcess(@ModelAttribute Process processForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        processForm = session.getAttribute("processForm") == null ? processForm : (Process) session.getAttribute("processForm");
        PageHelper.startPage(pageNum, pageSize);
        List<Process> searchResult = processService.findSearch(processForm);
        PageInfo<Process> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("processList", searchResult);
        model.addAttribute("processForm", processForm);
        model.addAttribute("pageInfo", pageInfo);
        return "process/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Process processForm = new Process();
        processForm.setProductList(processService.findProductList());
        model.addAttribute("process", processForm);
        return "process/form";
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Process processForm = processService.findProcessById(id);
        processForm.setProductList(processService.findProductList());
        model.addAttribute("process", processForm);
        return "process/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Process process, Model model, RedirectAttributes redirectAttributes) {
        if (process.getProductId() == null) {
            model.addAttribute("message", "请选择商品在保存");
            if (process.getProcessId() != null) {
                process.setProductList(processService.findProductList());
                model.addAttribute("process", process);
                return "process/form"; // 返回编辑表单视图
            } else {
                Process processForm = new Process();
                processForm.setProductList(processService.findProductList());
                model.addAttribute("process", processForm); // 返回新增表单视图
                return "process/form";
            }
        }

        if(process.getProcessOldId() == null || !(process.getProcessId().equals(process.getProcessOldId()) && process.getProductId().equals(process.getProductOldId()))) {
            int count = processService.findProcessCount(process.getProductId(), process.getProcessId());
            if(count > 0) {
                model.addAttribute("message", "当前商品对应的工序号已经存在，请换一个商品或换一个工序号在保存");
                if (process.getProcessId() != null) {
                    process.setProductList(processService.findProductList());
                    model.addAttribute("process", process);
                    return "process/form"; // 返回编辑表单视图
                } else {
                    Process processForm = new Process();
                    processForm.setProductList(processService.findProductList());
                    model.addAttribute("process", processForm); // 返回新增表单视图
                    return "process/form";
                }
            }
        }

        if (process.getProcessKeyId() == null) {
            processService.save(process);
            redirectAttributes.addFlashAttribute("messageSuccess", "工序信息登入成功");
        } else {
            processService.update(process);
            redirectAttributes.addFlashAttribute("messageSuccess", "工序信息更新成功");
        }
        return "redirect:/process/search";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        processService.delete(id);
        return "redirect:/process/search";
    }

}
