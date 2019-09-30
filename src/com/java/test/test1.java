package com.java.test;

import java.text.ParseException;
import java.util.Date;

import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;

import com.java.utils.CommonUtils;
import com.java.utils.DateUtils;

public class test1 {
	public static void main(String[] args) throws ParseException {
		String start = "08:00";
    	String end = "08:00";
    	Date k = CommonUtils.stringToDate(start, "HH:mm");
    	Date j = CommonUtils.stringToDate(end, "HH:mm");
    	boolean equals = k.equals(j);
		System.out.println("是否相等 :" + equals);
		
		String start1 = "08:00";
    	String end1 = "09:00";
    	Date k1 = CommonUtils.stringToDate(start1, "HH:mm");
    	Date j1 = CommonUtils.stringToDate(end1, "HH:mm");
    	boolean after = k1.after(j1);
		System.out.println("前大还是后大：" + after);
		
		String start2 = "10:34";
    	String end2 = "10:00";
    	Date k2 = CommonUtils.stringToDate(start2, "HH:mm");
    	Date j2 = CommonUtils.stringToDate(end2, "HH:mm");
    	boolean after2 = k2.after(j2);
		System.out.println("前大还是后大：" + after2);
		
		String aaa = "24:00";
		String bbb = "00:00";
    	Date a = CommonUtils.stringToDate(aaa, "HH:mm");
    	Date b = CommonUtils.stringToDate(bbb, "HH:mm");
		Date now = new Date();
		String nowStr = CommonUtils.dateFormat(now, "HH:mm");
		Date n = CommonUtils.stringToDate(nowStr, "HH:mm");
		long timec = n.getTime();
		long timea = k2.getTime();
		long timeb = j2.getTime();
		
		long time1 = a.getTime();
		long time2 = b.getTime();
		
		if (after2) {//开始时间>结束时间
			if (timec > timea && timec < time1) {
				System.out.println("有交集");
			}
			if (timec > time2 && timec < timeb) {
				System.out.println("有交集");
			}
		}
		
		
		String dd = "2019-09-12 11:00:00";
		Date cur = CommonUtils.stringToDate(dd, null);
		long time = now.getTime();
		long curtime = cur.getTime();
		long res = time - curtime;
		int min = (int) (res/(1000*60));
		System.out.println("相差的分钟数 ：" + min);
	}

}
