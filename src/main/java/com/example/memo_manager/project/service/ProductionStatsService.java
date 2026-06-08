package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Product;
import com.example.memo_manager.project.dto.ProductionStats;
import com.example.memo_manager.project.mapper.ProductionStatsMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionStatsService {

    @Autowired
    private ProductionStatsMapper productionStatsMapper;


    public List<Department> findDepList() {
        return productionStatsMapper.findDepList();
    }

    public List<Product> findProductList() {
        return productionStatsMapper.findProductList();
    }

    public List<ProductionStats> findSearch(ProductionStats productionStatsForm) {
        return productionStatsMapper.findSearch(productionStatsForm);
    }

    public void delete(Integer id) {
        productionStatsMapper.delete(id);
    }

    public List<String> save(ProductionStats productionStats) {
        List<String> result = new ArrayList<>();
        Product productInfo = productionStatsMapper.findProductInfo(productionStats.getProductId());
        if(productInfo == null) {
            result.add("商品信息取得失败");
            return result;
        }
        ProductionStats productionStatsInsert = new ProductionStats();
        // 3600 作为 BigDecimal
        BigDecimal hour = new BigDecimal("3600");
        // 每天工作时间
        BigDecimal workTimeSecond = productionStats.getWorkTime().multiply(hour);
        // 人均产量 = 每人每天工作总秒数÷测试一件衣服用时
        BigDecimal avgOutputPerPerson = workTimeSecond.divide(productInfo.getTotalTime(), 2, RoundingMode.HALF_UP);
        productionStatsInsert.setAvgOutputPerPerson(avgOutputPerPerson);
        if (avgOutputPerPerson.compareTo(BigDecimal.ZERO) == 0) {
            result.add("人均产量计算结果为0，请确认数据");
            return result;
        }
        // 计划日产量 = 出勤人数×人均产量
        BigDecimal plannedDailyOutput = productionStats.getPeopleNumber().multiply(avgOutputPerPerson);
        productionStatsInsert.setPlannedDailyOutput(plannedDailyOutput);
        if (plannedDailyOutput.compareTo(BigDecimal.ZERO) == 0) {
            result.add("计划日产量计算结果为0，请确认数据");
            return result;
        }
        // 订单交付天数 = 商品总数÷计划日产量
        BigDecimal orderDeliveryDays = productInfo.getTotleNum().divide(plannedDailyOutput, 2, RoundingMode.HALF_UP);
        productionStatsInsert.setOrderDeliveryDays(orderDeliveryDays);
        if (orderDeliveryDays.compareTo(BigDecimal.ZERO) == 0) {
            result.add("订单交付天数计算结果为0，请确认数据");
            return result;
        }
        // 人均单价 = 商品总价格÷人数
        BigDecimal pricePerUnit = productInfo.getTotalPrice().divide(productionStats.getPeopleNumber(), 2, RoundingMode.HALF_UP);
        productionStatsInsert.setPricePerUnit(pricePerUnit);
        if (pricePerUnit.compareTo(BigDecimal.ZERO) == 0) {
            result.add("人均单价计算结果为0，请确认数据");
            return result;
        }
        // 人均时间=总秒时÷出勤人数
        BigDecimal timePerPerson = productInfo.getTotalTime().divide(productionStats.getPeopleNumber(), 2, RoundingMode.HALF_UP);
        if (timePerPerson.compareTo(BigDecimal.ZERO) == 0) {
            result.add("人均时间计算结果为0，请确认数据");
            return result;
        }
        productionStatsInsert.setTimePerPerson(timePerPerson);
        productionStatsInsert.setDepId(productionStats.getDepId());
        productionStatsInsert.setProductId(productionStats.getProductId());
        productionStatsInsert.setUseDate(productionStats.getUseDate());
        productionStatsInsert.setPeopleNumber(productionStats.getPeopleNumber());
        productionStatsInsert.setWorkTime(productionStats.getWorkTime());
        productionStatsMapper.insert(productionStatsInsert);
        return null;
    }
}
