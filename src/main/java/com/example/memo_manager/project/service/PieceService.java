package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Piece;
import com.example.memo_manager.project.dto.PieceRequest;
import com.example.memo_manager.project.dto.PieceShow;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.mapper.PieceMapper;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PieceService {

  @Autowired
  private PieceMapper pieceMapper;


  public List<Department> findDepList() {
    return pieceMapper.findDepList();
  }

  public List<Product> findProductList() {
    return pieceMapper.findProductList();
  }

  public Map<String, List<Piece>> findSearch(PieceRequest pieceForm) {
    List<Piece> pieceList = pieceMapper.searchPieceList(pieceForm);
    Map<String, List<Piece>> groupedByUserName = null;
    if (pieceList != null && !pieceList.isEmpty()) {
      // 过滤并分组，同时保持原有顺序
      groupedByUserName = pieceList.stream()
          .filter(piece -> piece.getUserName() != null && !piece.getUserName().isEmpty()) // 过滤
          .collect(Collectors.groupingBy(Piece::getUserName,
              LinkedHashMap::new, // 使用LinkedHashMap来保持插入顺序
              Collectors.toList())); // 收集结果

      for (List<Piece> value : groupedByUserName.values()) {
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal sumTime = BigDecimal.ZERO;
        for (Piece piece : value) {
          // 单价
          BigDecimal unitPrice = piece.getUnitPrice();
          // 时间
          BigDecimal costTime = piece.getCostTime();
          // 数量
          Integer useNum = piece.getUseNum();
          if (unitPrice != null && useNum != null) {
            // 单价
            BigDecimal processPrice = unitPrice.multiply(BigDecimal.valueOf(useNum));
            sum = sum.add(processPrice);
            piece.setPriceProcess(String.valueOf(processPrice));
          } else {
            piece.setPriceProcess("0");
          }
          if (costTime != null && useNum != null) {
            // 时间
            BigDecimal timePrice = costTime.multiply(BigDecimal.valueOf(useNum));
            sumTime = sumTime.add(timePrice);
            piece.setTimeProcess(String.valueOf(timePrice));
          } else {
            piece.setTimeProcess("0");
          }
        }

        if (!value.isEmpty()) {
          // 总价
          value.getFirst().setPriceAllSum(String.valueOf(sum));
          // 总时间
          value.getFirst().setTimeAllSum(String.valueOf(sumTime));
        }

      }
    }

    return groupedByUserName;
  }

  public List<Piece> findPieceSearch(PieceShow pieceShowForm) {
    return pieceMapper.findPieceSearch(pieceShowForm);
  }

  public PieceShow findPieceById(Integer id) {
    return pieceMapper.findPieceById(id);
  }

  public void save(PieceShow pieceShow) {
    pieceMapper.insert(pieceShow);
  }

  public void update(PieceShow pieceShow) {
    pieceMapper.update(pieceShow);
  }

  public void delete(Integer id) {
    pieceMapper.delete(id);
  }

  public List<Process> findProcessList(Integer productId) {
    return pieceMapper.findProcessList(productId);
  }

  public List<String> checkParment(PieceShow pieceShow) {
    List<String> messageList = new ArrayList<>();
    Integer userId = pieceMapper.findUserId(pieceShow.getUserName());
    if(userId == null) {
      messageList.add("员工姓名在员工表里不存在，请先登入");
      return messageList;
    }
    pieceShow.setUserId(userId);
    Integer count = pieceMapper.findPieceCount(pieceShow);
    if(count != null && count > 0) {
      messageList.add("（姓名-商品-工序-日期）一样的数据已经存在");
    }
    return messageList;
  }


}
