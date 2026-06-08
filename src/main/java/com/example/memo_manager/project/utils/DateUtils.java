package com.example.memo_manager.project.utils;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    // 定义默认日期格式
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将LocalDate格式化为字符串。
     *
     * @param date LocalDate对象
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * 将LocalDateTime格式化为字符串。
     *
     * @param dateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * 解析字符串到LocalDate。
     *
     * @param dateString 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    /**
     * 解析字符串到LocalDateTime。
     *
     * @param dateTimeString 日期时间字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT));
    }

    /**
     * 验证字符串是否是有效的日期格式。
     *
     * @param dateString 日期字符串
     * @param dateFormat 日期格式
     * @return 如果输入是有效日期则返回true，否则返回false
     */
    public static boolean isValidDate(String dateString, String dateFormat) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 使用默认日期格式验证字符串是否是有效的日期。
     *
     * @param dateString 日期字符串
     * @return 如果输入是有效日期则返回true，否则返回false
     */
    public static boolean isValidDate(String dateString) {
        return isValidDate(dateString, DEFAULT_DATE_FORMAT);
    }
}