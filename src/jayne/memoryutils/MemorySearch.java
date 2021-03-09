package jayne.memoryutils;

import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @description: 内存搜索类
 * @author: Jayne
 * @date: 2020-02-10 21:27
 */
public class MemorySearch {


    /**
     * 范围搜索
     *
     * @param data     要搜索的数据
     * @param dataType 数据类型
     * @param <T>      数据类型
     * @return 搜索到的数据地址集合
     */
    public static <T> ArrayList<Long> rangeSearch(T data, DataType dataType) {
        ArrayList<Long> results = null;
        //解析maps
        AllTools.rangeAddrs.clear();
        switch (AllTools.memRange) {
            case A:
                MapsPrase.praseA();
                break;
            case AS:
                MapsPrase.praseAs();
                break;
            case CA:
                MapsPrase.praseCa();
                break;
            case CB:
                MapsPrase.praseCb();
                break;
            case CD:
                MapsPrase.praseCd();
                break;
            case CH:
                MapsPrase.praseCh();
                break;
            case S:
                MapsPrase.praseS();
                break;
            case JH:
                MapsPrase.praseJh();
                break;
            case V:
                MapsPrase.praseV();
                break;
            case XA:
                MapsPrase.praseXa();
                break;
            case XS:
                MapsPrase.praseXs();
                break;
            case ALL:
                MapsPrase.praseAll();
                break;
        }

        int dataSize = dataType.getValue();
        byte[] bytes = null;
        byte[] temp = new byte[dataSize];

        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {

            for (int i = 0; i < AllTools.rangeAddrs.size() / 3; i++) {
                Long seek = Long.parseLong(AllTools.rangeAddrs.get(i * 3), 16);
                ByteBuffer buff = ByteBuffer.allocate(Integer.parseInt(AllTools.rangeAddrs.get(i * 3 + 2)));//分配字节空间
                channel.read(buff, seek);//从seek处开始读
                bytes = buff.array();
                for (int j = 0; j < bytes.length / dataSize; j++) {
                    System.arraycopy(bytes, j * dataSize, temp, 0, dataSize);
                    if (getDataInfo(temp, dataType) == data) {
                        //数据匹配成功,记录数据地址
                        results.add(seek + (j * dataSize));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\033[31;0m范围搜索失败!\033[0m");
            e.printStackTrace();
        }
        return results;
    }

    /**
     * 偏移搜索: 传入地址集, 搜索指定偏移值是否为指定值, 保存是的地址集并返回
     *
     * @param data     要匹配的数据
     * @param offSet   偏移值
     * @param addrs    要匹配的地址集
     * @param dataType 数据类型
     * @param <T>      数据类型
     * @return 地址集
     */
    public static <T> ArrayList<Long> offsetSearch(T data, int offSet, ArrayList<Long> addrs, DataType dataType) {
        byte[] bytes = null;
        ArrayList<Long> resultAddrs = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {
            int dataSize = dataType.getValue();
            ByteBuffer buff = ByteBuffer.allocate(dataSize);//分配字节空间
            for (int i = 0; i < addrs.size(); i++) {
                buff.clear();
                channel.read(buff, addrs.get(i) + offSet);//从seek处读字节出来
                bytes = buff.array();
                if (getDataInfo(bytes, dataType) == data) {
                    resultAddrs.add(addrs.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println("\033[31;0m偏移搜索失败!\033[0m");
            e.printStackTrace();
        }
        return resultAddrs;
    }


    /**
     * 获取地址信息
     *
     * @param seek     指针
     * @param offset   偏移
     * @param dataType 数据类型
     * @return
     */
    public static <T> T getAddrInfo(Long seek, int offset, DataType dataType) {

        byte[] bytes = null;
        T data = null;

        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buff = ByteBuffer.allocate(dataType.getValue());//分配字节空间
            channel.read(buff, seek + offset);//从seek处读字节出来
            bytes = buff.array();

        } catch (Exception e) {
            e.printStackTrace();
        }

        data = getDataInfo(bytes, dataType);

        return data;

    }

    /**
     * 获取地址集信息
     *
     * @param seek     指针
     * @param offset   偏移
     * @param dataType 数据类型
     * @return
     */
    public static <T> ArrayList<T> getAddrInfos(ArrayList<Long> seek, int offset, DataType dataType) {

        byte[] bytes = null;
        ArrayList<T> data = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buff = ByteBuffer.allocate(dataType.getValue());//分配字节空间
            for (int i = 0; i < seek.size(); i++) {
                buff.clear();
                channel.read(buff, seek.get(i) + offset);//从seek处读字节出来
                bytes = buff.array();
                data.add(getDataInfo(bytes, dataType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 通过socket向指定端口持续传输地址集中的内容
     *
     * @param seek     地址集
     * @param offset   偏移
     * @param dataType 数据类型
     * @param port     端口
     * @return socket线程, 用于关闭线程
     */
    public static ServerThread putAddrInfos(ArrayList<Long> seek, int offset, DataType dataType, int port) {

        byte[] bytes = null;
        ServerThread thread = null;

        try (RandomAccessFile raf = new RandomAccessFile(AllTools.MEMPATHNAME, "rw");
             FileChannel channel = raf.getChannel();
             // 创建服务端socket
             ServerSocket serverSocket = new ServerSocket(port);
             // 等待客户端连接
             Socket socket = serverSocket.accept()) {
            ByteBuffer buff = ByteBuffer.allocate(dataType.getValue());//分配字节空间
            thread = new ServerThread(socket);
            thread.setState(true);
            thread.start();
            ArrayList dataList = new ArrayList();
            while (thread.isState()) {
                dataList.clear();
                for (int i = 0; i < seek.size(); i++) {
                    buff.clear();
                    channel.read(buff, seek.get(i) + offset);//从seek处读字节出来
                    bytes = buff.array();
                    dataList.add(getDataInfo(bytes, dataType));
                }
                thread.setBytes(dataList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thread;
    }

    /**
     * 根据不同类型返回对应类型值
     *
     * @param bytes    要转换的字节
     * @param dataType 数据类型
     * @param <T>      返回结果类型
     * @return 结果信息
     */
    private static <T> T getDataInfo(byte[] bytes, DataType dataType) {
        T data = null;
        //根据不同类型返回对应类型值
        switch (dataType) {
            case BYTE:
                byte value = bytes[0];
                data = (T) AllTools.typeShift(value);
                break;
            case WORD:
                long l = ByteUtils.byteArrToLong(bytes);
                data = (T) AllTools.typeShift(l);
                break;
            case DWORD:
                int i = ByteUtils.byteArrToInt(bytes);
                data = (T) AllTools.typeShift(i);
                break;
            case FLOAT:
                float v = ByteUtils.byteArrToFloat(bytes);
                data = (T) AllTools.typeShift(v);
                break;
            case QWORD:
                short i1 = ByteUtils.byteArrToShort(bytes);
                data = (T) AllTools.typeShift(i1);
                break;
            case DOUBLE:
                double v1 = ByteUtils.byteArrToDouble(bytes);
                data = (T) AllTools.typeShift(v1);
                break;
            default:
                data = (T) AllTools.typeShift(0);
                break;
        }
        return data;
    }

}
