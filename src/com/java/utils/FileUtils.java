package com.java.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 创建文件（文件存在，则删除）
     *
     * @param fileName
     * @return
     */
    public static File makeNewFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                //文件存在，删除
                file.delete();
            } else {
                //文件不存在，先创建路径
                String filePath = fileName.substring(0, fileName.lastIndexOf(System.getProperty("file.separator")));
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }

            //创建文件
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文件（文件存在，则删除）
     *
     * @param fileName
     * @param content
     * @return
     */
    public static int makeNewFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                //文件存在，删除
                file.delete();
            } else {
                //文件不存在，先创建路径
                String filePath = fileName.substring(0, fileName.lastIndexOf(System.getProperty("file.separator")));
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }

            //创建文件
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            org.apache.commons.io.FileUtils.writeStringToFile(file, content, "UTF-8");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 文件追加内容
     *
     * @param fileName
     * @param content
     * @return
     */
    public static int fileAppend(String fileName, String content) {
        BufferedWriter writer = null;
        try {
            //创建路径
            String filePath = fileName.substring(0, fileName.lastIndexOf(System.getProperty("file.separator")));
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            //创建文件
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            //追加日志
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "UTF-8"));
            if (file.length() != 0) {//空文件
                writer.write("\r\n");
            }
            writer.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * 文件追加内容
     *
     * @param fileName
     * @param content
     * @return
     */
    public static int fileAppendGBK(String fileName, String content) {
        BufferedWriter writer = null;
        try {
            //创建路径
            String filePath = fileName.substring(0, fileName.lastIndexOf(System.getProperty("file.separator")));
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            //创建文件
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            //追加日志
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true), "GBK"));
            if (file.length() != 0) {//空文件
                writer.write("\r\n");
            }
            writer.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * 把串内容写到文件指定行上，如果插入行与该行内容相同则覆盖，不同则插入到该行<br>
     * 如果插入行号<=0，则在文件最后追加一行
     *
     * @param fileName 文件名称，全路径
     * @param content 插入的行内容
     * @param insertLineNum 要插入到文件的行号
     * @return
     */
    public static int insertAssignedLineWithCompare(String fileName, String content,
            int insertLineNum) {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            // 创建路径
            String filePath = fileName.substring(0, fileName.lastIndexOf(System
                    .getProperty("file.separator")));
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(fileName);
            if (!file.exists()) {
                // 文件不存在，新建文件，直接写入内容为一行
                file.createNewFile();

                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true), "UTF-8"));
                writer.write(content);
            } else {
                // 文件已存在
                if (insertLineNum <= 0) {
                    // 把内容追加到文件最后
                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file, true), "UTF-8"));
                    if (file.length() != 0) {// 非空文件
                        writer.write("\r\n");//换行
                    }
                    writer.write(content);
                } else {
                    // 把内容插入到指定行
                    reader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "UTF-8"));

                    StringBuilder front = new StringBuilder();// 第insertLineNum行前面的内容
                    StringBuilder back = new StringBuilder();// 第insertLineNum行后面的内容
                    String insertLineStr = "";// 第insertLineNum行的内容
                    String currentLineStr = reader.readLine();// 每一行内容
                    int currentLineNum = 0;// 当前行号
                    while (currentLineStr != null && !"".equals(currentLineStr)) {
                        currentLineNum++;
                        if (currentLineNum < insertLineNum) {
                            front.append(currentLineStr).append("\r\n");
                        } else if (currentLineNum == insertLineNum) {
                            insertLineStr = currentLineStr;
                        } else {
                            back.append(currentLineStr).append("\r\n");
                        }
                        currentLineStr = reader.readLine();
                    }

                    //currentLineNum目前为文件最大行号
                    if (currentLineNum < insertLineNum) {
                        // 写入行号大于文件最大行号，把内容追加到文件最后
                        front.append(content);
                    } else {
                        // 把内容追加到文件指定行
                        if (content.indexOf(insertLineStr) < 0) {
                            // 没有插入过此行，插入
                            front.append(content).append("\r\n").append(insertLineStr);
                            if (back.toString() != null && !"".equals(back.toString())) {
                                front.append("\r\n").append(back.substring(0, back.lastIndexOf("\r\n")));
                            }
                        } else {
                            // 插入过此行，覆盖
                            front.append(content);
                            if (back.toString() != null && !"".equals(back.toString())) {
                                front.append("\r\n").append(back.substring(0, back.lastIndexOf("\r\n")));
                            }
                        }
                    }

                    // 重新创建文件
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return -1;
                    }
                    file.delete();
                    file.createNewFile();

                    writer = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file, true), "UTF-8"));
                    writer.write(front.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return 0;
    }

    /**
     * 获取文件
     *
     * @param filePath
     * @return
     */
    public static File getFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String getFileContent(File file) {
        StringBuilder fileContent = new StringBuilder();
        try {
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line);
                }
                read.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileContent.toString();
    }

}
