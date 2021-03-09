package jayne.memoryutils;

import java.io.*;
import java.util.ArrayList;

/**
 * @description: 工具类
 * @author: Jayne
 * @date: 2020-02-09 13:54
 */


enum MemRange {//内存范围枚举
    JH, CA, CB, CD, CH, A, AS, S, V, XS, XA, ALL
}

enum ThreadNumber {//线程数枚举
    ONE, TWO, THREE, FOUR, FIVE
}

enum DataType {//数据类型枚举
    DWORD(4), FLOAT(4), DOUBLE(8), BYTE(1), QWORD(2), WORD(8);

    private final int value;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    DataType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class AllTools {
    static String PACKAGENAME = "";//包名
    static int PID = 0;//pid
    static ArrayList<String> rangeAddrs = new ArrayList();//当前内存范围地址集
    static MemRange memRange = MemRange.ALL; //当前内存范围
    static String MEMPATHNAME = "/proc/" + PID + "/mem";//mem文件路径

    /**
     * 初始化内存插件
     *
     * @param packageName 包名
     */
    public static void initUtil(String packageName) {
        // 设置包名
        if (packageName == null || packageName.length() <= 0) {
            System.out.println("\033[31;0m初始化失败!包名不正确!\033[0m");
            return;
        }
        PACKAGENAME = packageName;

        // 设置pid
        int processid = getProcessid();
        if (processid == 0) {
            System.out.println("\033[31;0m获取进程PID失败!\033[0m");
            return;
        }
        PID = processid;

        //inotify反调试
        try (FileWriter fileWriter = new FileWriter("/proc/sys/fs/inotify/max_queued_events");
             FileWriter fileWriter2 = new FileWriter("/proc/sys/fs/inotify/max_user_watches")) {
            fileWriter.write("0");
            fileWriter.flush();
            fileWriter2.write("0");
            fileWriter2.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置内存范围
     *
     * @param range 内存范围
     */
    public static void setMemRange(MemRange range) {
        // 设置内存范围
        memRange = range;
    }

    private static int getProcessid() {
        int pid = 0;

        try {
            Process exec = Runtime.getRuntime().exec(
                    "su -c pidof " + PACKAGENAME);
            InputStream inputStream = exec.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(inputStream));
            String str = buff.readLine();
            if (str == null || str.length() <= 0) {
                System.out.println("\033[31;0m获取进程PID失败!\033[0m");
                return 0;
            }
            pid = Integer.parseInt(str);
            System.out.println("\033[36;0m进程PID: " + pid + "\033[0m");
        } catch (Exception e) {
            System.out.println("\033[31;0m获取进程PID失败!\033[0m");
            e.printStackTrace();
        }
        return pid;
    }

    public static <T> T typeShift(T data) {
        return data;
    }

}
