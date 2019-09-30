package com.java.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.java.entity.User;

public class ExcelExport {
	
	@SuppressWarnings("unused")
	public static void export(ExcelParam excelParam, String fileName, HttpServletRequest request, /*String [] sheetName,*/ HttpServletResponse response) throws IOException {
        if (excelParam.widths == null) {
            excelParam.widths = new int[excelParam.headers.length];
            for (int i = 0; i < excelParam.headers.length; i++) {
                excelParam.widths[i] = excelParam.width;
            }
        }
        if (excelParam.ds_format == null) {
            excelParam.ds_format = new int[excelParam.headers.length];
            for (int i = 0; i < excelParam.headers.length; i++) {
                excelParam.ds_format[i] = 1;
            }
        }
        //创建一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        int rowCount = 0;
        int rowCount2 = 0;
        //创建一个sheet
        HSSFSheet sheet = wb.createSheet(fileName);
        //创建第二一个sheet
        //HSSFSheet sheet2 = wb.createSheet("说明");
        if (excelParam.headers != null) {
            HSSFRow row = sheet.createRow(rowCount);
            //HSSFRow row2 = sheet2.createRow(rowCount2);
            //表头样式
            HSSFCellStyle style = wb.createCellStyle();
            HSSFFont font = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            
            for (int i = 0; i < excelParam.headers.length; i++) {
                sheet.setColumnWidth(i, excelParam.widths[i]);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(excelParam.headers[i]);
                cell.setCellStyle(style);
            }
            
//            for (int i = 0; i < 3; i++) {
//                sheet2.setColumnWidth(i, excelParam.widths[0]);
//                HSSFCell cell = row2.createCell(i);
//                cell.setCellValue("校区类型");
//                cell.setCellStyle(style);
//            }
            rowCount++;
            //rowCount2++;
        }
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //表格主体  解析list
        for (int i = 0; i < excelParam.data.size(); i++) {  //行数
            HSSFRow row = sheet.createRow(rowCount);
            for (int j = 0; j < excelParam.headers.length; j++) {  //列数
                HSSFCell cell = row.createCell(j);
                cell.setCellValue(excelParam.data.get(i)[j]);
                cell.setCellStyle(style);
            }
            rowCount++;
        }
        //表2
//        for (int i = 1; i < 10; i++) {  //行数
//            HSSFRow row = sheet2.createRow(rowCount2);
//            for (int j = 0; j < 3; j++) {  //列数
//                HSSFCell cell = row.createCell(j);
//                cell.setCellValue("lisi");
//                cell.setCellStyle(style);
//            }
//            rowCount2++;
//        }
        //设置文件名
        //String fileName = "users.xls";
        response.setContentType("application/vnd.ms-excel");
    	String agent = request.getHeader("USER-AGENT");
		//response.setContentType("application/x-download");
		if(agent!=null && (agent.indexOf("Firefox")>-1 || agent.indexOf("Safari")>-1)){
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1"));
		}else{
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
		}
        response.setHeader("Pragma", "No-cache");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
	
	
	public static void exportToMany(ExcelParam excelParam, String fileName, HttpServletRequest request, /*String [] sheetName,*/ HttpServletResponse response) throws IOException {
        if (excelParam.widths == null) {
            excelParam.widths = new int[excelParam.headers.length];
            for (int i = 0; i < excelParam.headers.length; i++) {
                excelParam.widths[i] = excelParam.width;
            }
        }
        if (excelParam.ds_format == null) {
            excelParam.ds_format = new int[excelParam.headers.length];
            for (int i = 0; i < excelParam.headers.length; i++) {
                excelParam.ds_format[i] = 1;
            }
        }
        //创建一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        int rowCount = 0;
        //循环工作表Sheet
        for (int numSheet = 0; numSheet < 3/*wb.getNumberOfSheets()*/; numSheet++) {
            HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
            wb.setSheetName(numSheet, "校区信息-" + String.valueOf(numSheet));
            if (hssfSheet == null) {
                continue;
            }
            if (excelParam.headers != null) {
                HSSFRow row = hssfSheet.createRow(rowCount);
                //表头样式
                HSSFCellStyle style = wb.createCellStyle();
                HSSFFont font = wb.createFont();
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                font.setFontHeightInPoints((short) 11);
                style.setFont(font);
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                
                for (int i = 0; i < excelParam.headers.length; i++) {
                	hssfSheet.setColumnWidth(i, excelParam.widths[i]);
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(excelParam.headers[i]);
                    cell.setCellStyle(style);
                }
                rowCount++;
            }
          //表格主体  解析list
            for (int i = 0; i < excelParam.data.size(); i++) {  //行数
                HSSFRow row = hssfSheet.createRow(rowCount);
                for (int j = 0; j < excelParam.headers.length; j++) {  //列数
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(excelParam.data.get(i)[j]);
                }
                rowCount++;
            }
        }
        response.setContentType("application/vnd.ms-excel");
    	String agent = request.getHeader("USER-AGENT");
		//response.setContentType("application/x-download");
		if(agent!=null && (agent.indexOf("Firefox")>-1 || agent.indexOf("Safari")>-1)){
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1"));
		}else{
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
		}
        response.setHeader("Pragma", "No-cache");
        OutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }
	
	public static void writeExcel(HSSFWorkbook workbook, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.ms-excel");
    	String agent = request.getHeader("USER-AGENT");
		//response.setContentType("application/x-download");
		if(agent!=null && (agent.indexOf("Firefox")>-1 || agent.indexOf("Safari")>-1)){
			response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("UTF-8"),"ISO8859-1"));
		}else{
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(fileName, "UTF-8"));
		}
        response.setHeader("Pragma", "No-cache");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
	}
	
	public ExcelExport() {
		
	}
	/**
	 * @Title: exportExcel
	 * @Description: 导出 学员缴费记录
	 * @param workbook 
	 * @param sheetNum (sheet的位置，0表示第一个表格中的第一个sheet)
	 * @param sheetTitle  （sheet的名称）
	 * @param headers    （表格的标题）
	 * @param result   （表格的数据）
	 * @param out  （输出流）
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void exportExcel(HSSFWorkbook workbook, int sheetNum,
			String sheetTitle, String[] headers, List<List<String>> result, List<String> clasInfo) throws Exception {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetNum, sheetTitle);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成标题样式  
		HSSFCellStyle style = workbook.createCellStyle();
		/***
		 *  设置这些样式
		 */
		//style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		//style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
 
		// 指定当单元格内容显示不下时自动换行
		style.setWrapText(true);
		
		
		
		/***
		 *  生成数据样式  
		 */
		HSSFCellStyle dataStyle = workbook.createCellStyle();
		// 设置这些样式
		//dataStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		//dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		// 生成一个字体
		HSSFFont dataFont = workbook.createFont();
		//dataFont.setColor(HSSFColor.BLACK.index);
		dataFont.setFontHeightInPoints((short) 10);
		//dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		dataStyle.setFont(dataFont);
 
		// 指定当单元格内容显示不下时自动换行
		dataStyle.setWrapText(true);
		
		//设置第一行 第二行格式
		//班级 学段  容量  教室
		HSSFRow row1 = sheet.createRow(0);
		HSSFCell cell0 = row1.createCell((short) 0);
		cell0.setCellValue("班级");
		HSSFCell cell2 = row1.createCell((short) 2);
		cell2.setCellValue("学段");
		HSSFCell cell4 = row1.createCell((short) 4);
		cell4.setCellValue("容量");
		HSSFCell cell6 = row1.createCell((short) 6);
		cell6.setCellValue("教室");
		cell0.setCellStyle(style);
		cell2.setCellStyle(style);
		cell4.setCellStyle(style);
		cell6.setCellStyle(style);
		//教师  开课时间  上课时间  学费
		HSSFRow row2 = sheet.createRow(1);
		HSSFCell cell20 = row2.createCell((short) 0);
		cell20.setCellValue("教师");
		HSSFCell cell22 = row2.createCell((short) 2);
		cell22.setCellValue("开课时间");
		HSSFCell cell24 = row2.createCell((short) 4);
		cell24.setCellValue("上课时间");
		HSSFCell cell26 = row2.createCell((short) 6);
		cell26.setCellValue("学费");
		cell20.setCellStyle(style);
		cell22.setCellStyle(style);
		cell24.setCellStyle(style);
		cell26.setCellStyle(style);
		//添加前两行数据
		HSSFCell cell1 = row1.createCell((short) 1);
		HSSFCell cell3 = row1.createCell((short) 3);
		HSSFCell cell5 = row1.createCell((short) 5);
		HSSFCell cell7 = row1.createCell((short) 7);
		HSSFCell cell21 = row2.createCell((short) 1);
		HSSFCell cell23 = row2.createCell((short) 3);
		HSSFCell cell25 = row2.createCell((short) 5);
		HSSFCell cell27 = row2.createCell((short) 7);
		cell1.setCellValue(clasInfo.get(0));
		cell3.setCellValue(clasInfo.get(1));
		cell5.setCellValue(clasInfo.get(2));
		cell7.setCellValue(clasInfo.get(3));
		cell21.setCellValue(clasInfo.get(4));
		cell23.setCellValue(clasInfo.get(5));
		cell25.setCellValue(clasInfo.get(6));
		cell27.setCellValue(clasInfo.get(7));
		cell1.setCellStyle(dataStyle);
		cell3.setCellStyle(dataStyle);
		cell5.setCellStyle(dataStyle);
		cell7.setCellStyle(dataStyle);
		cell21.setCellStyle(dataStyle);
		cell23.setCellStyle(dataStyle);
		cell25.setCellStyle(dataStyle);
		cell27.setCellStyle(dataStyle);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(2);
		for (int i = 0; i < headers.length; i++) {//设置列头及其样式
			HSSFCell cell = row.createCell((short) i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
		}
		// 遍历集合数据，产生数据行
		if (result != null) {
			int index = 3;
			for (List<String> m : result) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String str : m) {
					HSSFCell cell = row.createCell((short) cellIndex);
					cell.setCellValue(String.valueOf(str));
					cell.setCellStyle(style);
					cellIndex++;
					cell.setCellStyle(dataStyle);//设置数据样式
				}
				index++;
			}
		}
	}
	public static final String 用户名称 = "用户名称";
	public static final String 地址 = "地址";
	public static final String 年龄 = "年龄";
	public static final String 联系电话 = "联系电话";
	public static final String 生日 = "生日";
	public static final String 创建时间 = "创建时间";
	public static final String 修改时间 = "修改时间";

    public static final String[] IMPORT_EXCEL_PAYMENT_RECORDS = { 用户名称, 地址, 年龄, 联系电话 , 生日,
	 创建时间, 修改时间};
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws ParseException {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 20; i++) {
			User user = new User();
			user.setUser_name("张三" + i);
			user.setAddress("天津" + CommonUtils.getRandomInteger(0, 10));
			user.setAge(20 + i);
			user.setBirthday(CommonUtils.stringToDate("2000-10-10", "yyyy-MM-dd"));
			user.setMobile("18812345674");
			user.setCreate_time(new Date());
			user.setUpdate_time(new Date());
			userList.add(user);
		}
		try {
			OutputStream out = new FileOutputStream("C:\\sjwy\\test.xls");
			List<List<String>> data = new ArrayList<List<String>>();
			//for (int i = 1; i < 5; i++) {
			for (User user : userList) {
				List rowData = new ArrayList();
				rowData.add(String.valueOf(user.getUser_name()));
				rowData.add(String.valueOf(user.getAddress()));
				rowData.add(String.valueOf(user.getAge()));
				rowData.add(String.valueOf(CommonUtils.dateFormat(user.getBirthday(), "yyyy-MM-dd")));
				rowData.add(String.valueOf(user.getMobile()));
				rowData.add(String.valueOf(CommonUtils.dateFormat(user.getCreate_time(), "yyyy-MM-dd")));
				rowData.add(String.valueOf(CommonUtils.dateFormat(user.getUpdate_time(), "yyyy-MM-dd")));
				data.add(rowData);
			}
			//}
			//序号  学生名称  学生身份证号码  联系电话  动作  交易时间  金额  备注
			//String[] headers = { "ID", "用户名" };
			List<String> clasInfo = new ArrayList<String>();
			clasInfo.add("学前一班");
			clasInfo.add("6-8");
			clasInfo.add("20");
			clasInfo.add("505室");
			clasInfo.add("张三，李四");
			clasInfo.add("2018-05-05");
			clasInfo.add("周五 (8:00-10:100)");
			clasInfo.add("888");
			HSSFWorkbook workbook = new HSSFWorkbook();
			ExcelExport.exportExcel(workbook, 0, "上海", IMPORT_EXCEL_PAYMENT_RECORDS, data, clasInfo);
			ExcelExport.exportExcel(workbook, 1, "深圳", IMPORT_EXCEL_PAYMENT_RECORDS, data, clasInfo);
			ExcelExport.exportExcel(workbook, 2, "广州", IMPORT_EXCEL_PAYMENT_RECORDS, data, clasInfo);
			//原理就是将所有的数据一起写入，然后再关闭输入流。
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


//	public static void main(String[] args) {
//		try {
//			OutputStream out = new FileOutputStream("C:\\sjwy\\test2.xls");
//			List<List<String>> data = new ArrayList<List<String>>();
//			for (int i = 1; i < 5; i++) {
//				List rowData = new ArrayList();
//				rowData.add(String.valueOf(i));
//				rowData.add("张三"+ i);
//				rowData.add("男"+ i);
//				rowData.add(String.valueOf(i+10));
//				rowData.add("231219990101515"+ i);
//				rowData.add("张章"+ i);
//				rowData.add("1881234567"+ i);
//				rowData.add("无");
//				data.add(rowData);
//			}
//			//序号  学生名称  学生身份证号码  联系电话  动作  交易时间  金额  备注
//			//String[] headers = { "ID", "用户名" };
//			List<String> clasInfo = new ArrayList<String>();
//			clasInfo.add("学前一班");
//			clasInfo.add("6-8");
//			clasInfo.add("20");
//			clasInfo.add("505室");
//			clasInfo.add("张三，李四");
//			clasInfo.add("2018-05-05");
//			clasInfo.add("周五 (8:00-10:100)");
//			clasInfo.add("888");
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			//ExcelExport.exportStudentRosterExcel(workbook, 0, "一年一班", Constant.IMPORT_EXCEL_STU_ROSTER, data, clasInfo);
//			//ExcelExport.exportStudentRosterExcel(workbook, 1, "一年二班", Constant.IMPORT_EXCEL_STU_ROSTER, data, clasInfo);
//			//ExcelExport.exportStudentRosterExcel(workbook, 2, "一年三班", Constant.IMPORT_EXCEL_STU_ROSTER, data, clasInfo);
//			//原理就是将所有的数据一起写入，然后再关闭输入流。
//			workbook.write(out);
//			out.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static Workbook makeWorkbook(String sheetName, String[] columnName, List<String[]> exampleValueList, String extension)
			throws Exception {
		try {
			// 创建一个EXCEL
			Workbook wb = null;
			if ("xls".equals(extension)) {
				wb = new HSSFWorkbook();
			} else {
				wb = new XSSFWorkbook();
			}

			// 创建数据格式
			DataFormat dataFmt = wb.createDataFormat();
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat(dataFmt.getFormat("@"));
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// 得到默认页面上的Font
			Font font = wb.getFontAt((short) 0);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 11);
			cellStyle.setFont(font);

			// 生成sheet页名称
			Sheet sheet = wb.createSheet(sheetName);
			// 创建标题行
			Row titleRow = sheet.createRow(0);
			// 写入标题行上的列名称
			Cell cell = null;
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for (int i = 0; i < columnName.length; i++) {
				cell = titleRow.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sheet.setDefaultColumnStyle(i, cellStyle);
				//sheet.setColumnWidth(i, 50 * 256);
				try {
					if(map.containsKey(i)){
						map.put(i, map.get(i)>columnName[i].length()?map.get(i):columnName[i].length());
					}else{
						map.put(i, columnName[i].length());
					}
				} catch (Exception e) {
					map.put(i, 10);
				}
			}
			
			// 如果有示范值行，则进行输出
			if (exampleValueList != null && exampleValueList.size() > 0) {
				Row exampleDataRow = null;
				int rowIndex = 1;
				for (String[] exampleValue : exampleValueList) {
					if (exampleValue != null && exampleValue.length > 0) {
						exampleDataRow = sheet.createRow(rowIndex);
						cell = null;
						for (int i = 0; i < exampleValue.length; i++) {
							try {
								if(map.containsKey(i)){
									map.put(i, map.get(i)>exampleValue[i].length()?map.get(i):exampleValue[i].length());
								}else{
									map.put(i, exampleValue[i].length());
								}
							} catch (Exception e) {
								map.put(i, 10);
							}
							cell = exampleDataRow.createCell(i);
							// 设置单元格类型为文本
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue(exampleValue[i]);
							cell.setCellStyle(cellStyle);
						}
						rowIndex++;
					}
				}
			} else {
				CellStyle style = wb.createCellStyle();
				/***
				 *  设置第二行样式
				 */
				//style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
				//style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
				// 生成一个字体
				Font font2 = wb.createFont();
				font2.setColor(HSSFColor.DARK_RED.index);
				font2.setFontHeightInPoints((short) 12);
				font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				// 把字体应用到当前的样式
				style.setFont(font2);
				// 指定当单元格内容显示不下时自动换行
				style.setWrapText(true);
				
				// 创建第二行 合并单元格 输出提示信息
				Row rowx1 = sheet.createRow(1);
				
				CellRangeAddress crax0 =new CellRangeAddress(1, 1, 0, 7); // 起始行, 终止行, 起始列, 终止列
				//HSSFRow row1 = sheet.createRow(0);
				Cell cellx1 = rowx1.createCell((short) 0);
				rowx1.setHeight((short) ((short) 256*1.5));
				cellx1.setCellValue("您选择的班级没有职教老师,请重新选择其他班级;");
				sheet.addMergedRegion(crax0);
				cellx1.setCellStyle(style);
	
				RegionUtil.setBorderBottom(2, crax0, sheet, wb); // 下边框
				RegionUtil.setBorderLeft(2, crax0, sheet, wb); // 左边框
				RegionUtil.setBorderRight(2, crax0, sheet, wb); // 右边框
				RegionUtil.setBorderTop(2, crax0, sheet, wb); // 上边框	
			}
			for (Integer i : map.keySet()) {
				sheet.setColumnWidth(i, map.get(i) * 512+1536);
			}
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("生成工作簿对象时发生异常");
		}
	}
}
