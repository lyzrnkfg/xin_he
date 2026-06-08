package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Piece;
import com.example.memo_manager.project.dto.PieceInsert;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.dto.Student;
import com.example.memo_manager.project.mapper.ExcUploadMapper;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcUploadService {

  @Autowired
  private ExcUploadMapper excUploadMapper;


  public List<Student> findUserList() {
    return excUploadMapper.findUserList();
  }

  public List<Product> findProductList() {
    return excUploadMapper.findProductList();
  }

  public List<Process> findProcessList() {
    return excUploadMapper.findProcessList();
  }

  @Transactional(rollbackFor = Exception.class)
  public List<String> excInset(List<List<String>> sheetData, List<Student> userList,
      List<Product> productList) {

    List<PieceInsert> pieceList = excUploadMapper.findPieceList();
    List<String> messageList = new ArrayList<>();
    List<PieceInsert> pieceInsertList = new ArrayList<>();
    int index = 1;
    for (List<String> rowData : sheetData) {
      if (index == 1) {
        index = index + 1;
        continue;
      }
      PieceInsert pieceInsert = new PieceInsert();
      String userName = rowData.getFirst();
      Student studentCount = userList.stream().filter(
          x -> StringUtils.deleteWhitespace(x.getUserName())
              .equals(StringUtils.deleteWhitespace(userName))).findFirst().orElse(new Student());
      pieceInsert.setUserId(studentCount.getUserId());
      String productStyleName = rowData.get(1);
      String productName = rowData.get(2);
      Product productCount = productList.stream().filter(x ->
              StringUtils.deleteWhitespace(x.getProductStyleName())
                  .equals(StringUtils.deleteWhitespace(productStyleName))
                  && StringUtils.deleteWhitespace(x.getProductName())
                  .equals(StringUtils.deleteWhitespace(productName))).findFirst()
          .orElse(new Product());
      pieceInsert.setProductId(productCount.getProductId());
      pieceInsert.setProcessId(Integer.parseInt(rowData.get(3)));
      pieceInsert.setUseDate(rowData.get(5));
      // 如果用户ID商品ID日期工序号一直 则进行更新 否则插入
      if (pieceList.contains(pieceInsert)) {
        pieceInsert.setUseNum(Integer.parseInt(rowData.get(4)));
        excUploadMapper.updatePiece(pieceInsert);
        messageList.add("第" + index + "行进行了数量的更新");
      } else {
        pieceInsert.setUseNum(Integer.parseInt(rowData.get(4)));
        pieceInsertList.add(pieceInsert);
      }
      index = index + 1;
    }

    if (!pieceInsertList.isEmpty()) {
      excUploadMapper.insertPieceList(pieceInsertList);
    }
    messageList.add("成功的登入了" + pieceInsertList.size() + "行");
    return messageList;
  }

}
