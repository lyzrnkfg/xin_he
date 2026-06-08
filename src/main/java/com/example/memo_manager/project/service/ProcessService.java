package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.mapper.ProcessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessService {

    @Autowired
    private ProcessMapper processMapper;

    public Process findProcessById(Integer id) {
        return processMapper.findProcessById(id);
    }

    public List<Process> findSearch(Process processForm) {
        return processMapper.searchProcessList(processForm);
    }

    public void save(Process process) {
        processMapper.insert(process);
    }

    public void update(Process process) {
        processMapper.update(process);
    }

    public void delete(Integer id) {
        processMapper.delete(id);
    }

    public List<Product> findProductList() {
        return processMapper.findProductList();
    }

    public int findProcessCount(Integer productId, Integer processId) {
        return processMapper.findProcessCount(productId, processId);
    }

}
