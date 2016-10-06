package com.rafo.chess.resources.config.load;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.rafo.chess.resources.config.load.AbstractExcelLoader;
import com.rafo.chess.resources.config.load.ExcelLoader;

public class ExcelLoader extends AbstractExcelLoader {
	// 日志
	private static Logger log = Logger.getLogger(ExcelLoader.class);

	@Override
	public String[][] loadConfig(String configPath, String sheetName)
			throws Exception {
		log.info(String.format("Load Excel[%s] sheet[%s]", configPath,
				sheetName));
//		InputStream is = this.getClass().getClassLoader()
//				.getResourceAsStream(configPath);
//		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = getFile(configPath, sheetName);
		int rowCount = sheet.getPhysicalNumberOfRows();
		if (rowCount == 0) {
			return new String[0][0];
		}
		HSSFRow row = sheet.getRow(0);
		// cell[0][0]为空，此sheet跳过
		if (row.getCell(0) == null
				|| row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK) {
			return new String[0][0];
		}

		int colunmCount = row.getLastCellNum();
		int rowNum_legal = rowCount;
		// 判断实际列，第一列出现第一个空白时，认为列结束
		Object tmp;
		for (int i = 0; i < colunmCount; i++) {
			tmp = row.getCell(i);
			if (tmp == null || "".equals(tmp.toString())) {
				colunmCount = i;
			}
		}

		String[][] data = new String[rowCount][colunmCount];
		boolean end = false;
		for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
			if (end) {// 到达底端
				break;
			}

			row = sheet.getRow(rowIdx);
			data[rowIdx] = new String[colunmCount];
			if (row == null) {// 碰到整行为空时，直接退出
				rowNum_legal = rowIdx;
				end = true;
				break;
			}

			tmp = row.getCell(0);
			if (tmp == null || "".equals(tmp.toString())) {// 判断实际行，第一行出现第一个空白时，认为行结束
				rowNum_legal = rowIdx;
				end = true;
				break;
			}

			// 获取数据
			for (int columnIdx = 0; columnIdx < colunmCount; columnIdx++) {
				data[rowIdx][columnIdx] = this.getCellValue(row
						.getCell(columnIdx));
			}

		}

		// 整理数据
		if (rowNum_legal < rowCount) {
			String[][] back = new String[rowNum_legal][colunmCount];
			System.arraycopy(data, 0, back, 0, rowNum_legal);
			return back;
		} else {
			return data;
		}
	}

	private String getCellValue(HSSFCell cell) throws Exception {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		case HSSFCell.CELL_TYPE_STRING:
			return cell.toString();
		case HSSFCell.CELL_TYPE_NUMERIC:
			return NumberToTextConverter.toText(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_FORMULA:
			throw new IllegalAccessException(String.format(
					"cell [sheet:%s][colunm:%d][row:%d] type formula", cell
							.getSheet().getSheetName(), cell.getColumnIndex(),
					cell.getRowIndex()));
		default:
			throw new IllegalAccessException(String.format(
					"cell [sheet:%s][colunm:%d][row:%d] type error", cell
							.getSheet().getSheetName(), cell.getColumnIndex(),
					cell.getRowIndex()));
		}
	}
}
