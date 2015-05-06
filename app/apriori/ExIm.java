/*
 * sementara pake java. nanti dipindah ke scala.
 * dari : * https://gist.github.com/madan712/3912272
 */

package apriori;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ExIm {
    public static void importExcel(String judul) throws IOException {
        InputStream berkasExcel = new FileInputStream("/tmp/" + judul);
        XSSFWorkbook workbook = new XSSFWorkbook(berkasExcel);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()){
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()){
                cell = (XSSFCell) cells.next();
                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
                    System.out.println(cell.getStringCellValue());
                else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                    System.out.println(cell.getNumericCellValue());
            }
            System.out.println();
        }
    }
}