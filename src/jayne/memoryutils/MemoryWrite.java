package jayne.memoryutils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @description: 内存写入类
 * @author: Jayne
 * @date: 2020-02-11 15:58
 */
public class MemoryWrite {

    static boolean one = true;
    static boolean two = true;
    static boolean three = true;
    static boolean four = true;
    static boolean five = true;

    /**
     * 内存写入
     *
     * @param data     要写入的值
     * @param offSet   偏移量
     * @param addrs    要写入的地址集
     * @param dataType 数据类型
     * @param <T>      数据类型
     */
    public static <T> void addrWrite(T data, int offSet, ArrayList<Long> addrs, DataType dataType) {
        byte[] bytes = null;
        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buff = ByteBuffer.wrap(getDataBytes(data, dataType));

            for (int i = 0; i < addrs.size(); i++) {
                channel.write(buff, addrs.get(i) + offSet);//从seek处开始写字节
            }

        } catch (Exception e) {
            System.out.println("\033[31;0m内存写入失败!\033[0m");
            e.printStackTrace();
        }
    }

    /**
     * 冻结数据
     *
     * @param data         要冻结的数据
     * @param offSet       偏移
     * @param addrs        地址集
     * @param dataType     数据类型
     * @param ms           冻结间隔(毫秒)
     * @param threadNumber 线程号,最多支持5条线程
     * @param <T>          数据类型
     */
    public static <T> void freezeAddrWrite(T data, int offSet, ArrayList<Long> addrs, DataType dataType,
                                           int ms, ThreadNumber threadNumber) {
        byte[] bytes = null;
        ArrayList<Long> addrList = addrs;
        ThreadNumber tn = threadNumber;
        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buff = ByteBuffer.wrap(getDataBytes(data, dataType));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        boolean threadState = true;
                        while (threadState) {
                            switch (tn) {
                                case ONE:
                                    threadState = one;
                                    break;
                                case TWO:
                                    threadState = two;
                                    break;
                                case THREE:
                                    threadState = three;
                                    break;
                                case FOUR:
                                    threadState = four;
                                    break;
                                case FIVE:
                                    threadState = five;
                                    break;
                            }
                            for (int i = 0; i < addrList.size(); i++) {
                                channel.write(buff, addrList.get(i) + offSet);//从seek处开始写字节
                            }
                            Thread.sleep(ms);
                        }

                    } catch (Exception e) {
                        System.err.println("内存冻结失败!");
                        e.printStackTrace();
                    }

                }
            }).start();


        } catch (Exception e) {
            System.err.println("内存冻结失败!");
            e.printStackTrace();
        }
    }

    /**
     * 将数据转化成byte数组
     *
     * @param data     要转化的数据
     * @param dataType 数据类型
     * @return 转化完的字节数组
     */
    public static byte[] getDataBytes(Object data, DataType dataType) {
        byte[] bytes = null;
        //根据不同类型返回对应类型值
        switch (dataType) {
            case BYTE:
                bytes = new byte[]{(byte) data};
                break;
            case WORD:
                bytes = ByteUtils.longToByteArr((long) data);
                break;
            case DWORD:
                bytes = ByteUtils.intToByteArr((int) data);
                break;
            case FLOAT:
                bytes = ByteUtils.floatToByteArr((float) data);
                break;
            case QWORD:
                bytes = ByteUtils.shortToByteArr((short) data);
                break;
            case DOUBLE:
                bytes = ByteUtils.doubleToByteArr((double) data);
                break;
            default:
                bytes = ByteUtils.longToByteArr((long) 0);
                break;
        }
        return bytes;
    }

}
