package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.dto.ProductionStats;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductionStatsMapper {


  List<Department> findDepList();

  List<Product> findProductList();

  List<ProductionStats> findSearch(ProductionStats productionStatsForm);

  void delete(Integer id);

  Product findProductInfo(Integer productId);

  void insert(ProductionStats productionStatsInsert);
}
