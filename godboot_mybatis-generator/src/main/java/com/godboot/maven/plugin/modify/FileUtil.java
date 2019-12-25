package com.godboot.maven.plugin.modify;

import java.io.*;


/**
 * @author E-CHANGE
 */
public class FileUtil {
    public static void rmdirs(File path) {
        try {
            if (path.isFile()) {
                path.delete();
                return;
            }
            File[] subs = path.listFiles();
            if (subs.length == 0) {
                path.delete();
            } else {
                for (int i = 0; i < subs.length; i++) {
                    rmdirs(subs[i]);
                }
                path.delete();
            }
        } catch (Exception ex) {
        }
    }

    public static void xcopy(File src, File destine) {
        try {
            if ((!src.exists()) || (src.getCanonicalPath().equals(destine.getCanonicalPath()))) {
                return;
            }
        } catch (IOException ex) {
        }
        File[] chs = src.listFiles();
        for (int i = 0; i < chs.length; i++) {
            if (chs[i].isFile()) {
                File destineFile = new File(destine, chs[i].getName());
                copy(chs[i], destineFile);
            } else {
                File destineDir = new File(destine, chs[i].getName());
                destineDir.mkdirs();
                xcopy(chs[i], destineDir);
            }
        }
    }

    public static void copy(File src, File destine) {
        try {
            if ((!src.exists()) || (src.getCanonicalPath().equals(destine.getCanonicalPath()))) {
                return;
            }
            FileInputStream fins = new FileInputStream(src);
            copy(fins, destine);
            destine.setLastModified(src.lastModified());
        } catch (Exception e) {
        }
    }

    public static void copy(InputStream fins, File destine) {
        try {
            if (fins == null) {
                return;
            }
            destine.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(destine);
            byte[] buf = new byte[1024];
            int readLen;
            while ((readLen = fins.read(buf, 0, buf.length)) > 0) {
                fos.write(buf, 0, readLen);
            }
            fos.flush();
            fos.close();
            fins.close();
        } catch (Exception ex) {
        }
    }

    public static void move(File src, File destine) {
        try {
            if ((!src.exists()) || (src.getCanonicalPath().equals(destine.getCanonicalPath()))) {
                return;
            }
        } catch (IOException ex) {
        }
        copy(src, destine);
        src.delete();
    }

    public static String rename(String fromFilename, String toFilename) {
        String filename = toFilename;
        int tail_index = fromFilename.lastIndexOf(".");
        if (tail_index != -1) {
            String tail = fromFilename.substring(tail_index);
            filename = toFilename + tail;
        }
        return filename;
    }

    public static boolean makeDir(String dest) {
        boolean flag = true;
        try {
            File f = new File(dest);
            if (!f.mkdirs()) {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public static void showAllFiles(File dir)
            throws Exception {
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {
            System.out.println(fs[i].getAbsolutePath().substring(8));
            if (!fs[i].isDirectory()) {
                continue;
            }
            try {
                showAllFiles(fs[i]);
            } catch (Exception e) {
            }
        }
    }

    public static boolean CreateFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {
            System.out.println("目标文件所在路径不存在，准备创建。。。");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目录文件所在的目录失败！");
                return false;
            }
        }

        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            }
            System.out.println("创建单个文件" + destFileName + "失败！");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！");
        }
        return false;
    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        }
        System.out.println("创建目录" + destDirName + "成功！");
        return false;
    }

    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        try {
            if (dirName == null) {
                tempFile = File.createTempFile(prefix, suffix);
                return tempFile.getCanonicalPath();
            }
            File dir = new File(dirName);

            if ((!dir.exists()) &&
                    (!createDir(dirName))) {
                System.out.println("创建临时文件失败，不能创建临时文件所在目录！");
                return null;
            }

            tempFile = File.createTempFile(prefix, suffix, dir);
            return tempFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建临时文件失败" + e.getMessage());
        }
        return null;
    }

    public static String changeCharToLower(String str,int index){
        String changeChar = str.substring(index-1,index);
        String newStr = str.substring(index);
        return changeChar.toLowerCase() + newStr;
    }

    public static void main(String[] args) {
        /*try {
            showAllFiles(new File("c:\\logs"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dirName = "c:/test/test0/test1";
        createDir(dirName);

        String fileName = dirName + "/test2/testFile.txt";
        CreateFile(fileName);

        String prefix = "temp";
        String suffix = ".txt";
        for (int i = 0; i < 10; i++) {
            System.out.println("创建了临时文件:" + createTempFile(prefix, suffix, dirName));
        }*/
        System.out.println(changeCharToLower("Test",1));
    }
}
