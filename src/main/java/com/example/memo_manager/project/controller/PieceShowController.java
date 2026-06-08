package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.Piece;
import com.example.memo_manager.project.dto.PieceInsert;
import com.example.memo_manager.project.dto.PieceShow;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Student;
import com.example.memo_manager.project.service.PieceService;
import com.example.memo_manager.project.service.ProcessService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pieceShow")
public class PieceShowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PieceShowController.class);

    @Autowired
    private PieceService pieceService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String index(Model map) throws Exception {
        PieceShow pieceShowForm = new PieceShow();
        pieceShowForm.setProductList(pieceService.findProductList());
        pieceShowForm.setDepList(pieceService.findDepList());
        map.addAttribute("pieceShowForm", pieceShowForm);
        Optional.ofNullable(pieceShowForm).orElse(pieceShowForm);

        return "pieceShow/index";
    }

    @PostMapping("search")
    public String searchProcess(@ModelAttribute PieceShow pieceShowForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        session.setAttribute("pieceShowForm", pieceShowForm);
        pieceShowForm.setProductList(pieceService.findProductList());
        pieceShowForm.setDepList(pieceService.findDepList());
        PageHelper.startPage(pageNum, pageSize);
        List<Piece> searchResult = pieceService.findPieceSearch(pieceShowForm);
        PageInfo<Piece> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("pieceList", searchResult);
        model.addAttribute("pieceShowForm", pieceShowForm);
        model.addAttribute("pageInfo", pageInfo);
        return "pieceShow/index";
    }

    @GetMapping("search")
    public String searchPagePiece(@ModelAttribute PieceShow pieceShowForm, Model model, @RequestParam(value="pageNum",defaultValue="1")Integer pageNum, @RequestParam(value="pageSize",defaultValue="8")Integer pageSize) {
        pieceShowForm = session.getAttribute("pieceShowForm") == null ? pieceShowForm : (PieceShow) session.getAttribute("pieceShowForm");
        PageHelper.startPage(pageNum, pageSize);
        List<Piece> searchResult = pieceService.findPieceSearch(pieceShowForm);
        PageInfo<Piece> pageInfo = new PageInfo<>(searchResult);
        model.addAttribute("pieceList", searchResult);
        model.addAttribute("pieceShowForm", pieceShowForm);
        model.addAttribute("pageInfo", pageInfo);
        return "pieceShow/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        PieceShow pieceShowForm = new PieceShow();
        pieceShowForm.setProductList(pieceService.findProductList());
        pieceShowForm.setDepList(pieceService.findDepList());
        model.addAttribute("pieceShowForm", pieceShowForm);
        return "pieceShow/form";
    }

    @GetMapping("/findProcessSelect")
    @ResponseBody
    public List<Process> findProcessSelect(@RequestParam Integer productId) {
        List<Process> processList = new ArrayList<>();
        if(productId != null) {
            processList = pieceService.findProcessList(productId);
        }
        return processList;
    }

    @GetMapping("edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        PieceShow pieceShowForm = pieceService.findPieceById(id);
        pieceShowForm.setProductList(pieceService.findProductList());
        pieceShowForm.setDepList(pieceService.findDepList());
        pieceShowForm.setProcessList(pieceService.findProcessList(pieceShowForm.getProductId()));
        model.addAttribute("pieceShowForm", pieceShowForm);
        return "pieceShow/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute PieceShow pieceShow, Model model, RedirectAttributes redirectAttributes) {

        List<String> messageList = pieceService.checkParment(pieceShow);

        if(messageList != null && !messageList.isEmpty()) {
            model.addAttribute("messageList", messageList);
            pieceShow.setProductList(pieceService.findProductList());
            pieceShow.setDepList(pieceService.findDepList());
            pieceShow.setProcessList(pieceService.findProcessList(pieceShow.getProductId()));
            model.addAttribute("pieceShowForm", pieceShow);
            return "pieceShow/form";
        }

        if (pieceShow.getPieceId() == null) {
            pieceService.save(pieceShow);
            redirectAttributes.addFlashAttribute("messageSuccess", "工单信息登入成功");
        } else {
            pieceService.update(pieceShow);
            redirectAttributes.addFlashAttribute("messageSuccess", "工单信息更新成功");
        }
        return "redirect:/pieceShow/search";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        pieceService.delete(id);
        return "redirect:/pieceShow/search";
    }

}
