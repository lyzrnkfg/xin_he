package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.Piece;
import com.example.memo_manager.project.dto.PieceRequest;
import com.example.memo_manager.project.service.PieceService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/piece")
public class PieceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PieceController.class);

    @Autowired
    private PieceService pieceService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String index(Model map) throws Exception {
        PieceRequest pieceForm = new PieceRequest();
        map.addAttribute("pieceForm", pieceForm);
        Optional.ofNullable(pieceForm).orElse(pieceForm);

        return "piece/index";
    }

    @PostMapping("search")
    public String searchPiece(@ModelAttribute PieceRequest pieceForm, Model model) {
        session.setAttribute("pieceForm", pieceForm);
        pieceForm.setProductList(pieceService.findProductList());
        pieceForm.setDepList(pieceService.findDepList());
        Map<String, List<Piece>> searchResult = pieceService.findSearch(pieceForm);
        if(searchResult == null){
            model.addAttribute("message", "检索结果0件");
        }
        model.addAttribute("pieceMap", searchResult);
        model.addAttribute("pieceForm", pieceForm);
        return "piece/index";
    }

}
