package jayne.memoryutils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @description: maps文件分析类
 * @author: Jayne
 * @date: 2020-02-09 15:27
 */
public class MapsPrase {
    static String PATHNAME = "/proc/" + AllTools.PID + "/maps";//maps文件路径

    /**
     * 将内存地址储存到范围集中
     *
     * @param temp
     */
    private static void addRangeAddr(String temp) {
        String[] s = temp.split(" ")[0].split("-");
        AllTools.rangeAddrs.add(s[0]);
        AllTools.rangeAddrs.add(s[1]);
        Long l = Long.parseLong(s[1], 16) - Long.parseLong(s[0], 16);
        AllTools.rangeAddrs.add(l.intValue() + "");
    }

    /**
     * 解析all内存地址数据
     */
    public static void praseAll() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);

                // all内存地址
                if (temp.indexOf(" r") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析ca内存地址数据
     */
    public static void praseCa() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);

                // ca内存地址
                if (temp.indexOf("rw") != -1 && temp.indexOf("[anon:libc_malloc]") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析cb内存地址数据
     */
    public static void praseCb() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // cb内存地址
                if (temp.indexOf("rw") != -1 && temp.indexOf("[anon:.bss]") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析cd内存地址数据
     */
    public static void praseCd() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // cd内存地址
                if ((temp.indexOf("r--p") != -1 || temp.indexOf("rw-p") != -1 ||
                        temp.indexOf("rwxp") != -1) && temp.indexOf(".so") != -1 &&
                        (temp.indexOf("/data/app") != -1 || temp.indexOf("/data/data") != -1)) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析ch内存地址数据
     */
    public static void praseCh() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // ch内存地址
                if (temp.indexOf("rw") != -1 && temp.indexOf("[heap]") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析jh内存地址数据
     */
    public static void praseJh() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // jh内存地址
                if (temp.indexOf("rw") != -1 && (temp.indexOf("[anon:dalvik-main") != -1 || temp.indexOf("[anon:dalvik-large") != -1)) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析a内存地址数据
     */
    public static void praseA() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // a内存地址
                if ((temp.indexOf("r--p") != -1 || temp.indexOf("rw-p") != -1 ||
                        temp.indexOf("rwxp") != -1) && temp.length() < 42) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析s内存地址数据
     */
    public static void praseS() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // s内存地址
                if (temp.indexOf("rw") != -1 && temp.indexOf("[stack") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析as内存地址数据
     */
    public static void praseAs() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // as内存地址
                if ((temp.indexOf("r--s") != -1 || temp.indexOf("rw-s") != -1) &&
                        (temp.indexOf("dalvik") == -1 && temp.indexOf("/dev/ashmem") != -1)) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析v内存地址数据
     */
    public static void praseV() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // v内存地址
                if (temp.indexOf("rw-s") != -1 && temp.indexOf("kgsl-3d0") != -1) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xs内存地址数据
     */
    public static void praseXs() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);
                // xs内存地址
                if (temp.indexOf("r-xp") != -1 && (temp.indexOf("/system/") != -1 ||
                        temp.indexOf("/apex/") != -1 || temp.indexOf("[vectors]") != -1)) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xa内存地址数据
     */
    public static void praseXa() {
        try (InputStreamReader readStream = new InputStreamReader(new FileInputStream(PATHNAME));
             BufferedReader reader = new BufferedReader(readStream)) {
            String temp = null;
            int line = 0;//行号
            while ((temp = reader.readLine()) != null) {
                line++;
                System.out.println(line + ":" + temp);

                // xa内存地址
                if (temp.indexOf("r-xp") != -1 && temp.indexOf("/data/app") != -1 &&
                        temp.length() < 42) {
                    addRangeAddr(temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
