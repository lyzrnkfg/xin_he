package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Piece;
import com.example.memo_manager.project.dto.PieceRequest;
import com.example.memo_manager.project.dto.PieceShow;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PieceMapper {


    List<Department> findDepList();

    List<Product> findProductList();

    List<Piece> searchPieceList(PieceRequest pieceForm);

    List<Piece> findPieceSearch(PieceShow pieceShowForm);

    PieceShow findPieceById(Integer id);

    void insert(PieceShow pieceShow);

    void update(PieceShow pieceShow);

    void delete(Integer id);

    List<Process> findProcessList(Integer productId);

    Integer findUserId(String userName);

    Integer findPieceCount(PieceShow pieceShow);
}
