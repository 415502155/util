package com.java.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ZipStrUtils {
	
	public static String compressStr(String str) {
		if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
        } catch (IOException e) {
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                }
            }
        }
        return new Base64().encodeToString(out.toByteArray());
	}
	
    /**
     * @将Base64转换后的字符串转为字节数组
     *
     * @param compressedStr
     * @return
     */
    public static byte[] base2Byte(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        byte[] compressed = null;
        try {
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            //ByteArrayInputStream in = null;
            //GZIPInputStream ginzip = null;
            compressed = new Base64().decode(compressedStr);
            return compressed;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @解压缩 解压后的字节数组转为String
     *
     * @param compressed
     * @return
     */
    public static String unGzip(byte[] compressed) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream ginzip = null;
        ByteArrayInputStream in = null;
        String decompressed = null;
        try {
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException ex) {
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException ex) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
        return decompressed;
    }
	
	public static void main(String[] args) {
		/***
		 * @对长字符串压缩（base64）
		 */
		String str = "aaaaaaaaaaaaasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddfffffffffffffffffffffffffffffffffffffffghhhhhhhhhhhhhhhhhhhhhhhkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjlllllllllllllllllllllllllllllllllllllllllllllllllllllllqwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwqqqqqqqqqqqqqqqqqqqqqqqqqqqeeeeeeeeeeeeeeeeeeeerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrttttttttttttttttttttttttttttyyyyyyyyyyyyyyyyyyyyyyyyyyuuuuuuuuuuuuuuuuuuuuuuuuuuuuiiiiiiiiiiiiiiiiiiiiiiioooooooooooooooooooooooooppppppppppppppppppppppppppppppppppppppppppppppzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxccccccccccccccccccccccccccccccccccccccccccccvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm";
		String compressStr = compressStr(str);
		System.out.println(compressStr);
		/***
		 * @将压缩的字符串（base64）还原
		 */
		System.out.println(ZipStrUtils.unGzip(ZipStrUtils.base2Byte(compressStr)));
	}
}
