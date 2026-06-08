package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {


    Product findProductById(Integer id);

    List<Product> searchProductList(Product productForm);

    void insert(Product product);

    void update(Product product);

    void delete(Integer id);

    Integer findProductCount(Product product);
}
