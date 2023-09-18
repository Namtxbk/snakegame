package com.handler;
 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class ExcelReader {
    private boolean found = false;
    private Row targetRow;

    
    public void updateExcel(String name) throws IOException, URISyntaxException {
    	InputStream is = getClass().getResourceAsStream("/database/halloffame.xlsx");
    	XSSFWorkbook workbook = new XSSFWorkbook(is);
    	XSSFSheet sheet = workbook.getSheetAt(0); 
    	
    	Row record;
    	
    	FileOutputStream out = new FileOutputStream(new File(getClass().getResource("/database/halloffame.xlsx").toURI()));
    	for (int i=0;i<=sheet.getLastRowNum();i++)
		{
			record = sheet.getRow(i);
    		{
    			if (!record.getCell(0).getRichStringCellValue().getString().equals(name) && record.getRowNum() != sheet.getLastRowNum())
    				continue;
    			else 
    				if (i == sheet.getLastRowNum())
    				{
    					if (found)
    					{
	    					if (record.getCell(0).getRichStringCellValue().getString().equals(name))
	    					{
	    						record.getCell(1).setCellValue(record.getCell(1).getNumericCellValue()+1);
	        					targetRow = record;
	    					} 
	    					if (!targetRow.equals(record))
	    					{
	    						RichTextString tmpName = targetRow.getCell(0).getRichStringCellValue();
	    						int tmpNum = (int)targetRow.getCell(1).getNumericCellValue();
	    						
	    						targetRow.getCell(0).setCellValue(record.getCell(0).getRichStringCellValue());
	    						targetRow.getCell(1).setCellValue(record.getCell(1).getNumericCellValue());
	    						record.getCell(0).setCellValue(tmpName);
	    						record.getCell(1).setCellValue(tmpNum);
	    						break;
	    					}
    					}
    					else
    					{
    						sheet.createRow(sheet.getLastRowNum()+1);
    						sheet.getRow(sheet.getLastRowNum()).createCell(0).setCellValue(name);
    						sheet.getRow(sheet.getLastRowNum()).createCell(1).setCellValue(1);
    						break;
    					}
    				}
    				else if (record.getCell(0).getRichStringCellValue().getString().equals(name))
    				{
    					found = true;
    					record.getCell(1).setCellValue(record.getCell(1).getNumericCellValue()+1);
    					targetRow = record;
    				}
    		}
		}
    	workbook.write(out);
    	org.apache.commons.io.IOUtils.closeQuietly(out);
    	org.apache.commons.io.IOUtils.closeQuietly(is);
		workbook.close();
    }
 
    public String[][] getData() throws IOException {
    	InputStream is = getClass().getResourceAsStream("/database/halloffame.xlsx");
    	XSSFWorkbook workbook = new XSSFWorkbook(is);
    	XSSFSheet sheet = workbook.getSheetAt(0); 
    	int tmp, index=0;
    	String[][] data = new String[100][100]; 
    	
    	for (int i=sheet.getLastRowNum();i>=1;i--)
		{
    		data[index][0] = sheet.getRow(i).getCell(0).getRichStringCellValue().getString();
    		tmp = (int)sheet.getRow(i).getCell(1).getNumericCellValue();
    		data[index][1] = String.valueOf(tmp);
    		index++;
		}
    	org.apache.commons.io.IOUtils.closeQuietly(is);
    	workbook.close();
		return data;
    }
}