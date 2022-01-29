package textPOI;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * ClassName:POItest
 * Package:textPOI
 * Description:
 *
 * @Date:2022/1/28 17:10
 * @Author:Mars
 */
public class POItest {

    @Test
    public void test1() throws Exception {
        // 根据工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File("z:\\poi.xlsx")));
        // 获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        // 遍历工作表的获得行对象
        for (Row row : sheet) {
            // 遍历行对象获取单元格对象
            for (Cell cell : row) {
                // 获取单元格中的值
                String value = cell.getStringCellValue();
                System.out.println("value = " + value);
            }
        }
        xssfWorkbook.close();
    }

    @Test
    public void test2() throws Exception {
        // 在内存中创建一个Excel文件
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        // 创建一个工作表对象
        XSSFSheet sheet = xssfWorkbook.createSheet("mars");
        // 创建行对象
        XSSFRow title = sheet.createRow(0);
        // 创建列对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("北京");
        dataRow.createCell(2).setCellValue("18");

        // 创建一个输出流
        FileOutputStream outputStream = new FileOutputStream(new File("z:\\hello.xlsx"));
        xssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();

    }
}
