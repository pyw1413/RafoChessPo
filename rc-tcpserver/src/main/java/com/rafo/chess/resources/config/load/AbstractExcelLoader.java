package com.rafo.chess.resources.config.load;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.nutz.lang.Files;
import org.nutz.resource.NutResource;

import com.rafo.chess.resources.config.load.interfaces.ILoader;
import com.rafo.chess.resources.define.DataConfigManager;

public abstract class AbstractExcelLoader implements ILoader<String, HSSFWorkbook>{

	//public abstract HSSFWorkbook loadFile(String sheetName);
	
	public HSSFSheet getFile(String filename,String sheetname) throws IOException
	{
		Map<String, HSSFSheet> xlsMap = DataConfigManager.getInstance().getXlsMap();
		Sheet sheet = xlsMap.get(sheetname);
		if(sheet !=null)
		{
			return xlsMap.get(sheetname);
		}
		final InputStream is = Files.findFileAsStream(filename);
		NutResource resource = new NutResource() {
			@Override
			public InputStream getInputStream() throws IOException {
				return is;
			}
		};
		HSSFWorkbook workbook = new HSSFWorkbook(resource.getInputStream());
		int sheets = workbook.getNumberOfSheets();
		for(int i=0;i<sheets;i++)
		{
			HSSFSheet $sheet = workbook.getSheetAt(i);
			String name = $sheet.getSheetName();
			xlsMap.put(name, $sheet);
		}
		is.close();
		return xlsMap.get(sheetname);
	}
}
