package fileread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileAna//文件读取类 
{
	double m[][];
	List names;
	public List getNames() {
		return names;
	}
	public double[][] getM()
	{
		return m;
	}
	public void readFile(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String tempString = null;
            int t=0;
            while ((tempString = reader.readLine()) != null&&t<=100) {
                System.out.println(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	public void initM(String filePath,int size)
	{
		m=new double[size][size];
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String tempString = null;
            
            int index=0;
            System.out.println("start");
            while ((tempString = reader.readLine()) != null&&index<size) 
            {
            	for(int i=0;i<size-1;i++)
                {
                	String sim=tempString.substring(i*25,(i+1)*25-1);       
                	//System.out.println(i+"  "+sim+"over");
                	double temple=Double.parseDouble(sim);
                	//System.out.println(temple);
                	m[index][i]=temple;
                }
            	String last=tempString.substring(tempString.length()-24,tempString.length());
                //System.out.println(size-1+"  "+last+"over");
                double temple=Double.parseDouble(last);
            	//System.out.println(temple);
                m[index][size-1]=temple;
                index++;
                //System.out.println(index);
            }      
            System.out.println("over");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	public void initNames(String path)
	{
		InputStream is=null;
		try {
			is = new FileInputStream(new File(path));
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
	        List<String> result = new ArrayList<String>();
	        Sheet xssfSheet=xssfWorkbook.getSheetAt(0);
	        if (xssfSheet == null) {
	        	System.out.println("null");
	        	return;
	        }
	        // 处理当前页，循环读取每一行
	        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++)
	        {
	        	Row xssfRow = xssfSheet.getRow(rowNum);
	        	Cell cell1 = xssfRow.getCell(0);
	        	Cell cell2 =xssfRow.getCell(4);
	        	//System.out.println(rowNum+" "+cell1.getStringCellValue()+"  "+cell2.getStringCellValue());
	        	result.add(cell1.getStringCellValue()+" "+cell2.getStringCellValue());//服务名称和tag信息
	        }      
	        names=result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	public ArrayList<SevElements> getelement(String path)
	{
		ArrayList<SevElements> eles=new ArrayList<SevElements>();
		InputStream is=null;
		try {
			is = new FileInputStream(new File(path));
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
	        Sheet xssfSheet=xssfWorkbook.getSheetAt(0);
	        if (xssfSheet == null) {
	        	System.out.println("null");
	        	return eles;
	        }
	        // 处理当前页，循环读取每一行
	        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++)
	        {
	        	Row xssfRow = xssfSheet.getRow(rowNum);
	        	Cell cell1 = xssfRow.getCell(0);
	        	Cell cell2 =xssfRow.getCell(4);
	        	//System.out.println(rowNum+" "+cell1.getStringCellValue()+"  "+cell2.getStringCellValue());
	        	eles.add(new SevElements(cell1.getStringCellValue(),cell2.getStringCellValue(),rowNum-1));
	        }      
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return eles;	
	}
}

