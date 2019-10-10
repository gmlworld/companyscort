package com.gongml.companyscort.utils.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 网页抓取测试完成将数据导出成Excel表格
 *
 * @author caowb
 */
@Slf4j
public class ExportToExcel {
    public static void generateExcel(List<Map<String, Object>> data, String path) {
        if (data == null || data.isEmpty())
            return;
        java.util.List<String> colList = new ArrayList<>();
        for (String key : data.get(0).keySet()) {
            colList.add(key);
        }
        String[] cols = colList.toArray(new String[]{});
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("表格");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        HSSFCell cell = null;
        for (int j = 0; j < cols.length; j++) {
            cell = row.createCell(j);
            cell.setCellValue(cols[j]);
            cell.setCellStyle(style);
        }
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        for (int i = 0; i < data.size(); i++) {
            row = sheet.createRow((int) i + 1);
            Map<String, Object> map = data.get(i);
            for (int j = 0; j < cols.length; j++) {
                // 第四步，创建单元格，并设置值
                row.createCell(j).setCellValue(MapUtils.getString(map, cols[j]));
            }
        }
        // 第六步，将文件存到指定位置
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            wb.write(fout);
        } catch (Exception e) {
            log.error("文件输出到指定位置失败：" + e);
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                log.error("关闭输出流失败" + e);
            }
        }
    }

    /**
     * @param path
     * @param args
     * @auth denghc
     * @datetime 2016年12月7日-上午9:06:00
     * @desc 生成一个或多个sheet的excel
     */
    public static void generateExcelWithManySheets(String path, List<List<LinkedHashMap<String, Object>>> args) {
        if (args == null || args.size() <= 0) {
            return;
        }
        int i = args.size();
        List<LinkedHashMap<String, Object>> data = new ArrayList<>();
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int n = 0; n < args.size(); n++) {
            if (args.get(n) instanceof List) {
                data = (List<LinkedHashMap<String, Object>>) args.get(n);
                java.util.List<String> colList = new ArrayList<>();
                for (String key : data.get(0).keySet()) {
                    colList.add(key);
                }
                String[] cols = colList.toArray(new String[]{});
                // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
                HSSFSheet sheet = wb.createSheet("表格" + n);
                // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
                HSSFRow row = sheet.createRow((int) 0);
                // 第四步，创建单元格，并设置值表头 设置表头居中
                HSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER);
                HSSFCell cell = null;
                for (int j = 0; j < cols.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(cols[j]);
                    cell.setCellStyle(style);
                }
                // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
                for (int j = 0; j < data.size(); j++) {
                    row = sheet.createRow((int) j + 1);
                    LinkedHashMap<String, Object> map = data.get(j);
                    for (int k = 0; k < cols.length; k++) {
                        // 第四步，创建单元格，并设置值
                        row.createCell(k).setCellValue(MapUtils.getString(map, cols[k]));
                    }
                }
            }

        }
        // 第六步，将文件存到指定位置
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(path);
            wb.write(fout);
        } catch (Exception e) {
            log.error("文件输出到指定位置失败：" + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                log.error("关闭输出流失败：" + e);
                e.printStackTrace();
            }
        }
    }


}
