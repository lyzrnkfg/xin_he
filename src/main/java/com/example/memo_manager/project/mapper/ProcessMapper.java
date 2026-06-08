package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessMapper {

    Process findProcessById(Integer id);

    List<Process> searchProcessList(Process processForm);

    void insert(Process process);

    void update(Process process);

    void delete(Integer id);

    List<Product> findProductList();

    int findProcessCount(@Param("productId") Integer productId, @Param("processId") Integer processId);
}
