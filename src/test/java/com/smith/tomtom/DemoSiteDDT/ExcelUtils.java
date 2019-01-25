package com.smith.tomtom.DemoSiteDDT;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	private static XSSFWorkbook excelWorkBook;
	private static XSSFSheet excelWorkSheet;
	private static XSSFCell sheetCell;
	private static XSSFRow sheetRow;
	
	public static void setFile(String path, int sheetIndex) {
		try {
			FileInputStream file = new FileInputStream(path);
			excelWorkBook = new XSSFWorkbook(file);
			excelWorkSheet = excelWorkBook.getSheetAt(sheetIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getSheetRows() {
		return excelWorkSheet.getPhysicalNumberOfRows();
	}
	
	public static String getCellData(int row, int column) {
		sheetCell = excelWorkSheet.getRow(row).getCell(column);
		String cellData = sheetCell.getStringCellValue();
		return cellData;
	}
	
	public static void setCellData(String data, int row, int column) {
		try {
			sheetRow = excelWorkSheet.getRow(row);
			sheetCell = sheetRow.getCell(column, MissingCellPolicy.RETURN_BLANK_AS_NULL);
			
			if (sheetCell == null) {
				sheetCell = sheetRow.createCell(column);
				sheetCell.setCellValue(data);
			} else {
				sheetCell.setCellValue(data);
			}
			
			FileOutputStream fileOut = new FileOutputStream(Constant.DATA_PATH + Constant.DATA_FILE);
			excelWorkBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
