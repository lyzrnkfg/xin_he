package com.example.memo_manager.project.controller;

import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.dto.Student;
import com.example.memo_manager.project.dto.Process;
import com.example.memo_manager.project.service.ExcUploadService;
import com.example.memo_manager.project.utils.CheckParameter;
import com.example.memo_manager.project.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/excUpload")
public class excUplaodController {

  private static final Logger LOGGER = LoggerFactory.getLogger(excUplaodController.class);

  @Autowired
  private ExcUploadService excUploadService;

  @GetMapping
  public String index(Model map) throws Exception {
    return "excUpload/index";
  }

  @PostMapping("/read")
  public String readExcel(@RequestParam("file") MultipartFile file, Model model) {
    List<List<String>> sheetData = new ArrayList<>();
    List<String> messageList = new ArrayList<>();
    List<String> messageSuccessList = new ArrayList<>();
    try (InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream)) {

      // 获取名为 "工序" 的 Sheet
      Sheet sheet = workbook.getSheet("工序");
      if (sheet == null) {
        messageList.add("文件中工序页不存在");
      }

      // 遍历行
      if (sheet != null) {
        for (Row row : sheet) {
          List<String> rowData = new ArrayList<>();

          // 遍历列（单元格）
          for (Cell cell : row) {
            rowData.add(getCellValue(cell));  // 处理不同类型的单元格
          }

          sheetData.add(rowData);
        }
      }

    } catch (Exception e) {
      messageList.add("读取文件失败");
    }

    if (!messageList.isEmpty()) {
      model.addAttribute("messageList", messageList);
      return "excUpload/index";
    }

    List<Student> userList = excUploadService.findUserList();

    List<Product> productList = excUploadService.findProductList();

    List<Process> processList = excUploadService.findProcessList();

    messageList = validateData(sheetData, userList, productList, processList);

    if (messageList != null && !messageList.isEmpty()) {
      model.addAttribute("messageList", messageList);
    } else {
      messageSuccessList = excUploadService.excInset(sheetData, userList, productList);
      if (messageSuccessList != null && !messageSuccessList.isEmpty()) {
        model.addAttribute("messageSuccessList", messageSuccessList);
      }
    }

    return "excUpload/index";
  }

  /**
   * 获取单元格的值，避免数据格式问题
   */
  private String getCellValue(Cell cell) {
    if (cell == null) {
      return "";
    }

    DataFormatter dataFormatter = new DataFormatter(); // 处理格式化显示
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue();
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          // 处理日期格式
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          return dateFormat.format(cell.getDateCellValue());  // 转换为标准日期格式
        } else {
          return dataFormatter.formatCellValue(cell); // 避免数字变成 10.0
        }
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return dataFormatter.formatCellValue(cell); // 处理公式
      case BLANK:
        return "";
      default:
        return "";
    }
//    return switch (cell.getCellType()) {
//      case STRING -> cell.getStringCellValue();
//      case NUMERIC -> String.valueOf(cell.getNumericCellValue());
//      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
//      case FORMULA -> cell.getCellFormula();
//      case BLANK -> "";
//      default -> "";
//    };
  }

  private List<String> validateData(List<List<String>> sheetData, List<Student> userList,
      List<Product> productList, List<Process> processList) {
    List<String> messageList = new ArrayList<>();
    int index = 1;
    for (List<String> rowData : sheetData) {
      if (index == 1) {
        index = index + 1;
        continue;
      }
      if (!rowData.isEmpty()) {
        if (rowData.size() < 6) {
          messageList.add("请检查" + index + "行信息输入不全");
        }

        String userName = rowData.getFirst();
        Student studentCount = userList.stream().filter(
            x -> StringUtils.deleteWhitespace(x.getUserName())
                .equals(StringUtils.deleteWhitespace(userName))).findFirst().orElse(null);
        if (studentCount == null) {
          messageList.add("请检查" + index + "行员工未登入到员工信息中，请先登入");
        }
        String productStyleName = rowData.get(1);
        String productName = rowData.get(2);

        Product productCount = productList.stream().filter(x ->
                StringUtils.deleteWhitespace(x.getProductStyleName())
                    .equals(StringUtils.deleteWhitespace(productStyleName))
                    && StringUtils.deleteWhitespace(x.getProductName())
                    .equals(StringUtils.deleteWhitespace(productName))).findFirst()
            .orElse(new Product());
        if (productCount.getProductId() == null) {
          messageList.add("请检查" + index + "行商品未登入到商品信息中，请先登入");
        } else {
          String processId = rowData.get(3);
          Process processCount = processList.stream().filter(x ->
                  StringUtils.deleteWhitespace(x.getProcessId().toString())
                      .equals(StringUtils.deleteWhitespace(processId)) && StringUtils.deleteWhitespace(
                          x.getProductId().toString())
                      .equals(StringUtils.deleteWhitespace(productCount.getProductId().toString())))
              .findFirst().orElse(null);
          if (processCount == null) {
            messageList.add("请检查" + index + "行工序号未登入到工序信息中，请先登入");
          }
        }

        String useNum = rowData.get(4);
        if (StringUtils.isNotEmpty(useNum) && !CheckParameter.isNumeric(useNum)) {
          messageList.add("请检查" + index + "行作业数量，只能输入数字");
        }
        String useDate = rowData.get(5);
        if (StringUtils.isNotEmpty(useDate) && !DateUtils.isValidDate(useDate)) {
          messageList.add("请检查" + index + "行日期格式不对，默认格式yyyy-MM-dd");
        }

      }

      if (!messageList.isEmpty()) {
        return messageList;
      }

      index = index + 1;
    }

    return null;
  }

}
