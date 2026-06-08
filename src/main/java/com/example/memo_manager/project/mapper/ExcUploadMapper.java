package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.PieceInsert;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.dto.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExcUploadMapper {

    List<Student> findUserList();

    List<Product> findProductList();

    List<Process> findProcessList();

    List<PieceInsert> findPieceList();

    void updatePiece(PieceInsert pieceInsert);

    void insertPieceList(List<PieceInsert> pieceInsertList);
}
