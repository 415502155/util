package com.java.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


/**
 * @classname ExcelUtil.java
 * @description excel工具类
 * @author sunwei
 * @time 2018年1月11日
 */
public class ExcelUtil {

	/**
	 * 读取excel文件
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Workbook getExcelFromRequest(HttpServletRequest request) throws Exception {
		Workbook wb = null;

		// 检查form是否有enctype="multipart/form-data"
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 检查是否有文件上传
			Iterator<String> iter = multiRequest.getFileNames();
			if (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					wb = new HSSFWorkbook(file.getInputStream());
				}
			}
		}
		return wb;
	}

	/**
	 * 生成工作簿对象
	 * 
	 * @param arrSheetName sheet名称数组
	 * @param columnName 标题列名称数组
	 * @param extension 文件扩展名（xls或xlsx）
	 * @return 工作簿对象
	 * @throws Exception
	 */
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
			
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 得到默认页面上的Font
			Font font = wb.getFontAt((short) 0);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 11);
			cellStyle.setFont(font);

			// 生成sheet页名称
			Sheet sheet = wb.createSheet(sheetName);
			sheet.setColumnWidth(0, (int)((9.5 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(1, (int)((10 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(2, (int)((5 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(3, (int)((12 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(4, (int)((5 + 0.72) * 256));  //设置列宽，20个字符宽
			// 创建标题行
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 400);
			// 写入标题行上的列名称
			Cell cell = null;
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for (int i = 0; i < columnName.length; i++) {
				cell = titleRow.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//sheet.setDefaultColumnStyle(i, cellStyle);
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
						exampleDataRow.setHeight((short) 400);
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
			}
			for (Integer i : map.keySet()) {
				//sheet.setColumnWidth(i, map.get(i) * 512+1536);
				int colWidth = sheet.getColumnWidth(i)*2;
	            if(colWidth<255*256){
	                sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);    
	            }else{
	                sheet.setColumnWidth(i,6000 );
	            }
			}
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("生成工作簿对象时发生异常");
		}
	}

	/**
	 * 生成工作簿对象
	 * 
	 * @param arrSheetName sheet名称数组
	 * @param columnName 标题列名称数组
	 * @param extension 文件扩展名（xls或xlsx）
	 * @return 工作簿对象
	 * @throws Exception
	 */
	public static Workbook makeStudentListWorkbook(String sheetName, String[] columnName, List<String[]> exampleValueList, String extension)
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
			
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			// 得到默认页面上的Font
			Font font = wb.getFontAt((short) 0);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 11);
			cellStyle.setFont(font);
			// 生成sheet页名称
			Sheet sheet = wb.createSheet(sheetName);
			sheet.setDefaultColumnWidth((int) (12 + 0.72) * 256);
/*			sheet.setColumnWidth(0, (int)((9.5 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(1, (int)((10 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(2, (int)((5 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(3, (int)((12 + 0.72) * 256));  //设置列宽，20个字符宽
			sheet.setColumnWidth(4, (int)((5 + 0.72) * 256));  //设置列宽，20个字符宽
*/			// 创建标题行
			Row titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 400);
			// 写入标题行上的列名称
			Cell cell = null;
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for (int i = 0; i < columnName.length; i++) {
				cell = titleRow.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				//sheet.setDefaultColumnStyle(i, cellStyle);
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
						exampleDataRow.setHeight((short) 400);
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
			}
			for (Integer i : map.keySet()) {
				//sheet.setColumnWidth(i, map.get(i) * 512+1536);
				int colWidth = sheet.getColumnWidth(i)*2;
	            if(colWidth<255*256){
	                sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);    
	            }else{
	                sheet.setColumnWidth(i,6000 );
	            }
			}
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("生成工作簿对象时发生异常");
		}
	}
	
	
	/**
	 * 生成并输出Excel文件
	 * 
	 * @param response 响应对象
	 * @param workBook 工作簿对象
	 * @param fileName 输出文件名
	 * @throws Exception
	 */
	public static void makeAndOutExcelFile(HttpServletRequest request, HttpServletResponse response, Workbook workBook, String fileName) throws Exception {
		try {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			String userAgent = request.getHeader("User-Agent");
			// 针对IE或者以IE为内核的浏览器：  
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
            	fileName = URLEncoder.encode(fileName, "UTF-8");  
            } else {  
                // 非IE浏览器的处理：  
            	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");  
            }
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream fileOut = response.getOutputStream();
			workBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("输出excel时发生异常（IOException）");
		}
	}

	/**
	 * 生成并输出Excel文件
	 * 
	 * @param response 响应对象
	 * @param workBook 工作簿对象
	 * @param fileName 输出文件名
	 * @throws Exception
	 */
	public static void makeAndOutHSSFExcelFile(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook workBook, String fileName) throws Exception {
		try {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			String userAgent = request.getHeader("User-Agent");
			// 针对IE或者以IE为内核的浏览器：  
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
            	fileName = URLEncoder.encode(fileName, "UTF-8");  
            } else {  
                // 非IE浏览器的处理：  
            	fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");  
            }
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			OutputStream fileOut = response.getOutputStream();
			workBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("输出excel时发生异常（IOException）");
		}
	}
	
	/**
	 * 校验导入的支付Excel文件标题行是否为标准行
	 * 
	 * @param wb
	 * @return
	 * @throws Exception
	 * @author sw
	 */
	public static String verificationExcelHeadLine(Workbook wb, String[] columnName, Integer mustNum) throws Exception {
		if(null==mustNum){
			mustNum = columnName.length;
		}
		String result = "";

		try {
			Sheet sheet = wb.getSheetAt(0);

			Row row = sheet.getRow(0);
			
			if (row != null && row.getLastCellNum() > columnName.length) {
				int lastCellNum = row.getLastCellNum();
				for (int idx = 0; idx < lastCellNum; idx++) {
					String value = getCellValue(row.getCell(idx));
					if (idx < mustNum) {
						if (StringUtils.isBlank(value) || !columnName[idx].equals(value)) {
							result = "标题行第" + (idx + 1) + "列名称错误！";
							throw new Exception();
						}
					} else {
						if (idx == mustNum) {
							if (StringUtils.isBlank(value)) {
								result = "标题行缺少必填项";
								throw new Exception();
							}
						}
					}
				}
			} else {
				result = "上传文件首行不能为空或缺少必填项！";
			}
		} catch (Exception ex) {

		}
		return result;
	}
	
	/**
	 * 校验导入的教师工资Excel文件标题行是否为标准行
	 * 
	 * @param wb
	 * @return
	 * @throws Exception
	 * @author sw
	 */
	public static String verificationExcelHeadLine(Workbook wb, String[] columnName) throws Exception {
		String result = "";

		try {
			Sheet sheet = wb.getSheetAt(0);

			Row row = sheet.getRow(0);
			
			if (row != null && row.getLastCellNum() > columnName.length) {
				int lastCellNum = row.getLastCellNum();
				for (int idx = 0; idx < lastCellNum; idx++) {
					String value = getCellValue(row.getCell(idx));
					if (idx < 2) {
						if (StringUtils.isBlank(value) || !columnName[idx].equals(value)) {
							result = "标题行第" + (idx + 1) + "列名称错误！";
							throw new Exception();
						}
					} else {
						if (idx == 2) {
							if (StringUtils.isBlank(value)) {
								result = "标题不能仅有教师姓名和手机号码两列";
								throw new Exception();
							}
						}
					}
				}
			} else {
				result = "上传文件首行不能为空或不能仅有教师姓名和手机号码两列！";
			}
		} catch (Exception ex) {

		}
		return result;
	}
	
	/***
	 * 校验导入的班级列表Excel文件标题行是否为标准行
	 */
	public static String verificationClassExcelHeadLine(Workbook wb, String[] columnName) throws Exception {
		String result = null;

		try {
			Sheet sheet = wb.getSheetAt(0);

			Row row = sheet.getRow(0);
			System.out.println("row.getLastCellNum() :" +row.getLastCellNum());
			if (row != null && row.getLastCellNum() >= columnName.length) {
				int lastCellNum = row.getLastCellNum();
				for (int idx = 0; idx < lastCellNum; idx++) {
					String value = getCellValue(row.getCell(idx));
					if (idx < 21) {
						if (StringUtils.isBlank(value) || !columnName[idx].equals(value)) {
							result = "标题行第" + (idx + 1) + "列名称错误！";
							throw new Exception();
						}
					} else {
						if (idx == 21) {
							if (StringUtils.isBlank(value)) {
								result = "标题与导出班级的表格表头不一致";
								throw new Exception();
							}
						}
					}
				}
			} else {
				result = "上传文件首行不能为空或与导出班级的表格表头不一致！";
			}
		} catch (Exception ex) {

		}
		return result;
	}
	
	/***
	 * 校验导入的学员列表Excel文件标题行是否为标准行
	 */
	public static String verificationStudentExcelHeadLine(Workbook wb, String[] columnName) throws Exception {
		String result = null;

		try {
			Sheet sheet = wb.getSheetAt(0);

			Row row = sheet.getRow(0);
			
			if (row != null && row.getLastCellNum() >= columnName.length) {
				int lastCellNum = row.getLastCellNum();
				for (int idx = 0; idx < lastCellNum; idx++) {
					String value = getCellValue(row.getCell(idx));
					if (idx < 4) {
						if (StringUtils.isBlank(value) || !columnName[idx].equals(value)) {
							result = "标题行第" + (idx + 1) + "列名称错误！";
							throw new Exception();
						}
					} else {
						if (idx == 4) {
							if (StringUtils.isBlank(value)) {
								result = "标题与导出学员的表格表头不一致";
								throw new Exception();
							}
						}
					}
				}
			} else {
				result = "上传文件首行不能为空或与导出学员的表格表头不一致！";
			}
		} catch (Exception ex) {

		}
		return result;
	}


	/**
	 * 获取单元格中的值（去除字符串中所有的空格、回车、换行符、制表符及全角空格）
	 * 
	 * @param cell
	 * @return 以字符串表示的单元格中的值 无值返回空字符串
	 * @author fangjian 2014年4月4日下午7:59:33
	 */
	private static String getCellValue(Cell cell) {
		// if (cell != null) {
		// cell.setCellType(Cell.CELL_TYPE_STRING);
		// return replaceAllBlank(cell.getStringCellValue());
		// }
		// return "";
		String cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					cellValue = "";
					break;
				case Cell.CELL_TYPE_ERROR:
					cellValue = Byte.toString(cell.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					cellValue = replaceAllBlank(cellValue);
					break;
				// excel中日期也是数字，因此需要判断
				case Cell.CELL_TYPE_NUMERIC:
					// 处理日期格式、时间格式
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = null;
						if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
							sdf = new SimpleDateFormat("HH:mm");
						} else {// 日期
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						Date date = cell.getDateCellValue();
						cellValue = sdf.format(date);
					} else if (cell.getCellStyle().getDataFormat() == 58) {
						// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						double value = cell.getNumericCellValue();
						Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
						cellValue = sdf.format(date);
					} else {
						double value = cell.getNumericCellValue();
						//CellStyle style = cell.getCellStyle();
						DecimalFormat format = new DecimalFormat("0.0##");
						/*String temp = style.getDataFormatString();
						// 单元格设置成常规
						if (temp.equals("General")) {
							format.applyPattern("#.###");
						}*/
						cellValue = format.format(value);
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					cellValue = Boolean.toString(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					// 公式格式，读取计算后的值，而不是公式本身
					FormulaEvaluator evaluator = cell.getRow().getSheet().getWorkbook().getCreationHelper()
							.createFormulaEvaluator();
					CellValue cellValue4Formula = evaluator.evaluate(cell);

					// System.out.println(cellValue4Formula.getStringValue());

					switch (cellValue4Formula.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							cellValue = String.valueOf(cellValue4Formula.getBooleanValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							double value = cellValue4Formula.getNumberValue();
							DecimalFormat format = new DecimalFormat("0.0##");
							cellValue = format.format(value);
							break;
						case Cell.CELL_TYPE_STRING:
							cellValue = String.valueOf(cellValue4Formula.getStringValue());
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						case Cell.CELL_TYPE_ERROR:
							break;
						// CELL_TYPE_FORMULA will never happen
						case Cell.CELL_TYPE_FORMULA:
							break;
					}
					break;
				default:
					cellValue = "";
			}
		} else {
			cellValue = "";
		}

		if (StringUtils.isNotBlank(cellValue)) {
			cellValue = cellValue.trim();
		}
		return cellValue;
	}

	/**
	 * 获取单元格中的值（去除字符串中所有的空格、回车、换行符、制表符及全角空格）
	 * 
	 * @param cell
	 * @return 以字符串表示的单元格中的值 无值返回空字符串
	 * @author fangjian 2014年4月4日下午7:59:33
	 */
	private static String getCellValueTrimString(Cell cell) {
		// if (cell != null) {
		// cell.setCellType(Cell.CELL_TYPE_STRING);
		// return replaceAllBlank(cell.getStringCellValue());
		// }
		// return "";
		String cellValue = null;
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					cellValue = "";
					break;
				case Cell.CELL_TYPE_ERROR:
					cellValue = Byte.toString(cell.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					cellValue = cell.getRichStringCellValue().getString();
					cellValue = cellValue.trim();
					break;
				// excel中日期也是数字，因此需要判断
				case Cell.CELL_TYPE_NUMERIC:
					// 处理日期格式、时间格式
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						SimpleDateFormat sdf = null;
						if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
							sdf = new SimpleDateFormat("HH:mm");
						} else {// 日期
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						Date date = cell.getDateCellValue();
						cellValue = sdf.format(date);
					} else if (cell.getCellStyle().getDataFormat() == 58) {
						// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						double value = cell.getNumericCellValue();
						Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
						cellValue = sdf.format(date);
					} else {
						double value = cell.getNumericCellValue();
						//CellStyle style = cell.getCellStyle();
						DecimalFormat format = new DecimalFormat("0.0##");
						/*String temp = style.getDataFormatString();
						// 单元格设置成常规
						if (temp.equals("General")) {
							format.applyPattern("#.###");
						}*/
						cellValue = format.format(value);
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					cellValue = Boolean.toString(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					// 公式格式，读取计算后的值，而不是公式本身
					FormulaEvaluator evaluator = cell.getRow().getSheet().getWorkbook().getCreationHelper()
							.createFormulaEvaluator();
					CellValue cellValue4Formula = evaluator.evaluate(cell);

					// System.out.println(cellValue4Formula.getStringValue());

					switch (cellValue4Formula.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							cellValue = String.valueOf(cellValue4Formula.getBooleanValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							double value = cellValue4Formula.getNumberValue();
							DecimalFormat format = new DecimalFormat("0.0##");
							cellValue = format.format(value);
							break;
						case Cell.CELL_TYPE_STRING:
							cellValue = String.valueOf(cellValue4Formula.getStringValue());
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						case Cell.CELL_TYPE_ERROR:
							break;
						// CELL_TYPE_FORMULA will never happen
						case Cell.CELL_TYPE_FORMULA:
							break;
					}
					break;
				default:
					cellValue = "";
			}
		} else {
			cellValue = "";
		}

		if (StringUtils.isNotBlank(cellValue)) {
			cellValue = cellValue.trim();
		}
		return cellValue;
	}

	/**
	 * 去除字符串中所有的空格、回车、换行符、制表符及全角空格
	 * 
	 * @param str
	 * @return
	 * @author fangjian
	 */
	public static String replaceAllBlank(String str) {
		String returnStr = "";
		if (StringUtils.isNotBlank(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			returnStr = m.replaceAll("");
			returnStr = returnStr.replaceAll("　", "");
		}
		return returnStr;
	}
	
	
	public static Workbook makeSalaryExcelFileWithErrorInfo(String sheetName, String[] columnName, List<String[]> rowList,
			String extension) throws Exception {
		try {
			// 新建Excel工作簿
			Workbook wb = null;
			if ("xls".equals(extension)) {
				wb = new HSSFWorkbook();
			} else {
				wb = new XSSFWorkbook();
			}
			// 定义单元格样式
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// 得到默认页面上的Font
			Font font = wb.getFontAt((short) 0);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 11);
			cellStyle.setFont(font);
			// 定义错误信息字体
			HSSFFont redFont = (HSSFFont) wb.createFont();
			redFont.setFontName("宋体");
			redFont.setFontHeightInPoints((short) 11);
			redFont.setColor(HSSFColor.RED.index);// 颜色

			CellStyle errorCellStyle = wb.createCellStyle();
			errorCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			errorCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			errorCellStyle.setFont(redFont);

			// 新建sheet，使用之前sheet页的名称
			Sheet sheet = wb.createSheet(sheetName);

			// 创建标题行
			Row row = sheet.createRow(0);
			row.setRowStyle(cellStyle);

			// 写入标题行上的列名称
			Cell cell = null;
			for (int i = 0; i < columnName.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sheet.setDefaultColumnStyle(i, cellStyle);
				sheet.setColumnWidth(i, 50 * 256);
			}

			// 写入内容
			if (rowList != null && rowList.size() > 0) {
				Row exampleDataRow = null;
				int rowIndex = 1;
				for (String[] rowData : rowList) {
					if (rowData != null && rowData.length > 0) {
						exampleDataRow = sheet.createRow(rowIndex);
						cell = null;
						for (int i = 0; i < rowData.length; i++) {
							cell = exampleDataRow.createCell(i);
							cell.setCellType(Cell.CELL_TYPE_STRING);

							// 如果含有错误信息则使用红色字体，没有则使用普通字体
							String value = rowData[i];
							if (value.contains("错误信息")) {
								cell.setCellStyle(errorCellStyle);
							} else {
								// 设置单元格类型为文本
								cell.setCellStyle(cellStyle);
							}
							
							cell.setCellValue(value);
						}
						rowIndex++;
					}
				}
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("生成包含错误信息的Excel文件失败，请联系管理员！");
		}
	}

	/**
	 * 读取Excel工具
	 * @param wb 【必须】
	 * @param column 【可选】列头，非空时做列头校验
	 * @return 校验失败时返回null，无数据时返回new ArrayList<String[]>()
	 */
	public static List<String[]> getExcel(Workbook wb,String[] column) {
		List<String[]> list= new ArrayList<String[]>();
		String[] excelHeadLine = null;
		Sheet sheet = wb.getSheetAt(0);
		// 由于没有固定的格式所以根据第一行标题行获取数组长度
		Row titleRow = sheet.getRow(0);
		// 数据行的最大列数
		int cellLength = 0;
		for (int i = 0; i < titleRow.getLastCellNum(); i++) {
			String value = getCellValue(titleRow.getCell(i));
			if(null!=column&&(null==column[i]||!column[i].equals(value))){
				return null;
			}
			if (StringUtils.isNotBlank(value)) {
				cellLength++;
			}
		}
		// 项目数组
		excelHeadLine = new String[cellLength];
		for (int i = 0; i < cellLength; i++) {
			String value = getCellValue(titleRow.getCell(i));
			excelHeadLine[i] = value;
		}
		int lastRowNum = sheet.getLastRowNum();
		Row dataRow = null;
		for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
			//+1是为了给最后一行【错误信息】预留位置，否则汇报String[]的越界异常
			String[] row = new String[cellLength+1];
			dataRow = sheet.getRow(rowIndex);
			int isNull = 0;
			for (int i = 0; i < cellLength; i++) {
				row[i] = getCellValue(dataRow.getCell(i));
				if(StringUtils.isNotEmpty(row[i])){
					isNull++;
				}
			}
			if(isNull>0){
				list.add(row);
			}
		}
		return list;
	}

	/**
	 * 读取Excel工具
	 * @param wb 【必须】
	 * @param column 【可选】列头，非空时做列头校验
	 * @return 校验失败时返回null，无数据时返回new ArrayList<String[]>()
	 */
	public static List<String[]> getExcelTrimString(Workbook wb,String[] column) {
		List<String[]> list= new ArrayList<String[]>();
		String[] excelHeadLine = null;
		Sheet sheet = wb.getSheetAt(0);
		// 由于没有固定的格式所以根据第一行标题行获取数组长度
		Row titleRow = sheet.getRow(0);
		// 数据行的最大列数
		int cellLength = 0;
		for (int i = 0; i < titleRow.getLastCellNum(); i++) {
			String value = getCellValueTrimString(titleRow.getCell(i));
			if(null!=column&&(null==column[i]||!column[i].equals(value))){
				return null;
			}
			if (StringUtils.isNotBlank(value)) {
				cellLength++;
			}
		}
		// 项目数组
		excelHeadLine = new String[cellLength];
		for (int i = 0; i < cellLength; i++) {
			String value = getCellValueTrimString(titleRow.getCell(i));
			excelHeadLine[i] = value;
		}
		int lastRowNum = sheet.getLastRowNum();
		Row dataRow = null;
		for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
			//+1是为了给最后一行【错误信息】预留位置，否则汇报String[]的越界异常
			String[] row = new String[cellLength+1];
			dataRow = sheet.getRow(rowIndex);
			int isNull = 0;
			for (int i = 0; i < cellLength; i++) {
				row[i] = getCellValueTrimString(dataRow.getCell(i));
				if(StringUtils.isNotEmpty(row[i])){
					isNull++;
				}
			}
			if(isNull>0){
				list.add(row);
			}
		}
		return list;
	}
	
	
	
	/**
	 * 生成学生用餐统计信息excel
	 * @param columnName
	 * @param data
	 * @param fileName
	 * @param type
	 * @param extension
	 * @return
	 */
	public static Workbook makeDinerStatisticExcel(String[] columnName, List<String[]> data, String sheetName, String extension) {
		if (columnName != null && columnName.length > 0 && StringUtils.isNoneBlank(sheetName, extension)) {
			// 新建Excel工作簿
			Workbook wb = null;
			if ("xls".equals(extension)) {
				wb = new HSSFWorkbook();
			} else {
				wb = new XSSFWorkbook();
			}

			// 定义单元格样式
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			// 得到默认页面上的Font
			Font font = wb.getFontAt((short) 0);
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 11);
			cellStyle.setFont(font);
			// 定义错误信息字体
			HSSFFont redFont = (HSSFFont) wb.createFont();
			redFont.setFontName("宋体");
			redFont.setFontHeightInPoints((short) 11);
			redFont.setColor(HSSFColor.RED.index);// 颜色

			CellStyle errorCellStyle = wb.createCellStyle();
			errorCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			errorCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			errorCellStyle.setFont(redFont);

			Sheet sheet = wb.createSheet(sheetName);

			// 创建标题行
			Row row = sheet.createRow(0);
			row.setRowStyle(cellStyle);

			// 写入标题行上的列名称
			Cell cell = null;
			for (int i = 0; i < columnName.length; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columnName[i]);
				cell.setCellStyle(cellStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				sheet.setDefaultColumnStyle(i, cellStyle);
				sheet.setColumnWidth(i, 50 * 256);
			}

			if (data != null && data.size() > 0) {
				Row dataRow = null;
				int rowIndex = 1;
				// 写入内容
				// "身份唯一编码（）", "年级", "部门/班级", "姓名（）", "用餐天数"
				for (String[] rowData : data) {
					dataRow = sheet.createRow(rowIndex);
					cell = null;

					cell = dataRow.createCell(0);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(rowData[0]);

					cell = dataRow.createCell(1);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(rowData[1]);

					cell = dataRow.createCell(2);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(rowData[2]);

					cell = dataRow.createCell(3);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(rowData[3]);

					cell = dataRow.createCell(4);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(rowData[4]);

					rowIndex++;
				}
			}

			return wb;
		} else {
			return null;
		}
	}
	
	
    /**
     * 读取Excel内容
     * 
     * @param fis
     *            文件的流对象
     * @param exStr
     *            扩展名，xls和xlsx分别对应HSSFWorkbook和XSSFWorkbook对象
     * @param clazz
     *            反射类信息
     * @param fieldNames
     *            定制需要读取的属性
     * @return
     * @throws Exception
     */
/*    public static <T> List<T> readExcelContent(InputStream fis, String exStr, Class<T> clazz, List<String> fieldNames) throws Exception {
    	return readExcelContent(fis, exStr, clazz, fieldNames,false);
    }*/
    public static <T> List<T> readExcelXLSContent(Workbook wb, String exStr, Class<T> clazz, List<String> fieldNames,boolean onlyFristSheet) throws Exception {
        List<T> list = new ArrayList<T>();
//        Workbook wb = null;
//        if (".xls".equals(exStr)) {
//            wb = new HSSFWorkbook(fis);
//        } else {
//            wb = new XSSFWorkbook(fis);
//        }
        for (int i = 0; i < wb.getNumberOfSheets(); i++) { // 循环工作表
            if(onlyFristSheet && i==1){
            	break;
            }
        	Sheet sheet = wb.getSheetAt(i);
            // 从第二行开始, 保证第一行不可编辑的表头不参与循环
            for (int j = 1; j <= sheet.getLastRowNum(); j++) { // 循环行
                Row row = sheet.getRow(j);
                if (row != null) {
                    T t = (T) clazz.newInstance();
                    for (int k = 0; k < fieldNames.size(); k++) { // 循环单元格
                        Field field = clazz.getDeclaredField(fieldNames.get(k));
                        if (field.getType() == BigDecimal.class) { // 数值型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, new BigDecimal(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Integer.class) { // 整数型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Integer.parseInt(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Long.class) { // 整数型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Long.parseLong(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Double.class) { // 双精度浮点型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Double.parseDouble(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Float.class) { // 单精度浮点型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Float.parseFloat(getCellFormatValue(row.getCell(k))));
                        } else { // 字符型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, getCellFormatValue(row.getCell(k)));
                        }
                    }
                    list.add(t);
                }
            }
        }
        return list;
    }
    
    public static <T> List<T> readExcelXLSXContent(InputStream fis, String exStr, Class<T> clazz, List<String> fieldNames,boolean onlyFristSheet) throws Exception {
        List<T> list = new ArrayList<T>();
        Workbook wb = null;
        if (".xls".equals(exStr)) {
            wb = new HSSFWorkbook(fis);
        } else {
            wb = new XSSFWorkbook(fis);
        }
        for (int i = 0; i < wb.getNumberOfSheets(); i++) { // 循环工作表
            if(onlyFristSheet && i==1){
            	break;
            }
        	Sheet sheet = wb.getSheetAt(i);
            // 从第二行开始, 保证第一行不可编辑的表头不参与循环
            for (int j = 1; j <= sheet.getLastRowNum(); j++) { // 循环行
                Row row = sheet.getRow(j);
                if (row != null) {
                    T t = (T) clazz.newInstance();
                    for (int k = 0; k < fieldNames.size(); k++) { // 循环单元格
                        Field field = clazz.getDeclaredField(fieldNames.get(k));
                        if (field.getType() == BigDecimal.class) { // 数值型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, new BigDecimal(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Integer.class) { // 整数型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Integer.parseInt(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Long.class) { // 整数型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Long.parseLong(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Double.class) { // 双精度浮点型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Double.parseDouble(getCellFormatValue(row.getCell(k))));
                        } else if (field.getType() == Float.class) { // 单精度浮点型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, Float.parseFloat(getCellFormatValue(row.getCell(k))));
                        } else { // 字符型
                            clazz.getMethod("set" + getFirstUpCaseString(fieldNames.get(k)), field.getType()).invoke(t, getCellFormatValue(row.getCell(k)));
                        }
                    }
                    list.add(t);
                }
            }
        }
        return list;
    }
    
    /** 首字母大写 **/
    public static String getFirstUpCaseString(String str) {
        char[] chs = str.toCharArray();
        chs[0] -= 32;
        return String.valueOf(chs);
    }
    
    public static String getCellFormatValueEx(Cell cell) {
        String str = getCellFormatValue(cell);
        if (StringUtils.isBlank(str)) {
            return null;
        } else {
            return str;
        }
    }
    
    /**
     * 根据Cell类型设置数据
     * 
     * @param cell
     * @return
     */
    public static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式

                    // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    // cellvalue = cell.getDateCellValue().toLocaleString();

                    // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);

                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    DecimalFormat df = new DecimalFormat("#.########");
                    cellvalue = String.valueOf(df.format(cell.getNumericCellValue()));
                }
                break;
            }
            // 如果当前Cell的Type为STRING
            case Cell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                // cellvalue = cell.getRichStringCellValue().getString();
                cellvalue = cell.getStringCellValue();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue.trim();

    }
    
    /** 详细表表头样式 **/
    public static CellStyle getCustomCellStyle(Workbook workbook) {
        // 生成一个样式，用来设置标题样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }
}
