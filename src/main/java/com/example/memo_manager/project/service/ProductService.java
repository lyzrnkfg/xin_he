package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 根据 id 查询 Student
     * @param id
     * @return
     */
    public Product findProductById(Integer id) {
        return productMapper.findProductById(id);
    }

    public List<Product> findSearch(Product productForm) {
        return productMapper.searchProductList(productForm);
    }

    public void save(Product product) {
        productMapper.insert(product);
    }

    public void update(Product product) {
        productMapper.update(product);
    }

    public void delete(Integer id) {
        productMapper.delete(id);
    }

    public Integer findProductCount(Product product) {
        return productMapper.findProductCount(product);
    }
}
