package jayne.memoryutils;

/**
 * @description: 主要用于执行字节数组和基本数据类型之间的互相转换
 * 比如字节数组转int，和int转字节数组
 * java是属于大端字节序的，也就是高位放在低地址处
 * 所有的操作都是针对的大端字节序
 * @author: Jayne
 * @date: 2020-02-09 15:28
 */
public class ByteUtils {

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000", "0001", "0010", "0011",
                    "0100", "0101", "0110", "0111",
                    "1000", "1001", "1010", "1011",
                    "1100", "1101", "1110", "1111"};


    /**
     * 字节数组转short
     *
     * @param bytes 需要转换的字节数组
     * @return short
     */
    public static short byteArrToShort(byte[] bytes) {
        int FF = 0xff;
        int first = (bytes[1] & FF) << 8;
        short result = (short) (first | (bytes[0] & FF));
        return result;
        //可以直接return (short) ((bytes[0] << 8) | bytes[1]);
        //之所以向上面那么写，是为了方便看的清楚
    }

    /**
     * short转字节数组
     *
     * @param value 待转换的short
     * @return 字节数组
     */
    public static byte[] shortToByteArr(short value) {
        byte highByte = (byte) (value >> 8);//获取高位的字节
        byte lowByte = (byte) value;//获取低位的字节
        byte[] result = new byte[2];
        result[0] = highByte;
        result[1] = lowByte;
        return result;
    }

    /**
     * 字节数组转int
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的int
     */
    public static int byteArrToInt(byte[] bytes) {
        int FF = 0xff;
        //假设字节数组 05 0A 07 04
        int h1 = (bytes[3] & FF) << 24;//左移24位之后，h1为05 00 00 00
        int h2 = (bytes[2] & FF) << 16;//左移16位之后，h2为00 0A 00 00
        int h3 = (bytes[1] & FF) << 8;//左移8位之后，h3为00 00 07 00
        int h4 = bytes[0] & FF;//h4为00 00 00 04
        int result = h1 | h2 | h3 | h4;
        return result;
    }

    /**
     * int转字节数组
     *
     * @param value 待转换的int
     * @return 转换之后的字节数组
     */
    public static byte[] intToByteArr(int value) {
        //假设原来的int的16进制为 05 0A 07 04
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);//右移24位之后，返回值00 00 00 05,强转之后变成05
        bytes[1] = (byte) (value >> 16);//右移16位之后，返回值00 00 05 0A,强转之后变成0A
        bytes[2] = (byte) (value >> 8);//右移8位之后，返回值00 05 0A 07,强转之后变成07
        bytes[3] = (byte) value;
        return bytes;
    }

    /**
     * 把字节数组转换成long
     * 这个的处理手法和前面和int和short都有一点差异
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的long
     */
    public static long byteArrToLong(byte[] bytes) {
        long FF = 0xff;
        //假设字节数组为08 0A 01 03 05 07 02 0B
        long b0 = bytes[7] & FF;//00 00 00 00 00 00 00 08
        long h0 = b0 << 56;//08 00 00 00 00 00 00 00
        long b1 = bytes[6] & FF;//00 00 00 00 00 00 00 0A
        long h1 = b1 << 48;//00 0A 00 00 00 00 00 00
        long b2 = bytes[5] & FF;
        long b3 = bytes[4] & FF;
        long b4 = bytes[3] & FF;
        long b5 = bytes[2] & FF;
        long b6 = bytes[1] & FF;
        long b7 = bytes[0] & FF;
        long h2 = b2 << 40;
        long h3 = b3 << 32;
        long h4 = b4 << 24;
        long h5 = b5 << 16;
        long h6 = b6 << 8;
        long h7 = b7;
        return h0 | h1 | h2 | h3 | h4 | h5 | h6 | h7;
    }

    /**
     * long转换成字节数组
     *
     * @param value 待转换的long
     * @return 转换之后的字节数组
     */
    public static byte[] longToByteArr(long value) {
        //假设待转换的long为08 0A 01 03 05 07 02 0B
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (value >> 56);//右移之后,00 00 00 00 00 00 00 08,强转之后08
        bytes[1] = (byte) (value >> 48);//右移之后,00 00 00 00 00 00 08 0A,强转之后0A
        bytes[2] = (byte) (value >> 40);//右移之后,00 00 00 00 00 08 0A 01,强转之后01
        bytes[3] = (byte) (value >> 32);
        bytes[4] = (byte) (value >> 24);
        bytes[5] = (byte) (value >> 16);
        bytes[6] = (byte) (value >> 8);
        bytes[7] = (byte) value;
        return bytes;
    }

    /**
     * 将字节数组转换成float
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的float
     */
    public static float byteArrToFloat(byte[] bytes) {
        int FF = 0xff;
        //假设字节数组 05 0A 07 04
        int b0 = bytes[3] & FF;// 00 00 00 05
        int b1 = bytes[2] & FF;// 00 00 00 0A
        int b2 = bytes[1] & FF;
        int b3 = bytes[0] & FF;
        int h0 = b0 << 24;// 05 00 00 00
        int h1 = b1 << 16;// 00 0A 00 00
        int h2 = b2 << 8;
        int h3 = b3;
        int h = h0 | h1 | h2 | h3;
        return Float.intBitsToFloat(h);
    }


    /**
     * float转字节数组
     *
     * @param value 待转换的float
     * @return 转换之后的字节数组
     */
    public static byte[] floatToByteArr(float value) {
        int i = Float.floatToIntBits(value);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) (i);
        return bytes;
    }

    /**
     * 将字节数组转换成double
     *
     * @param bytes 待转换的字节数组
     * @return 转换出来的double
     */
    public static double byteArrToDouble(byte[] bytes) {
        //假设待转换的字节数组为05 0A 07 04 0B 00 03 01
        long FF = 0xFF;
        long b0 = bytes[7] & FF;// 00 00 00 00 00 00 00 05
        long b1 = bytes[6] & FF;// 00 00 00 00 00 00 00 0A
        long b2 = bytes[5] & FF;
        long b3 = bytes[4] & FF;
        long b4 = bytes[3] & FF;
        long b5 = bytes[2] & FF;
        long b6 = bytes[1] & FF;
        long b7 = bytes[0] & FF;
        long h0 = b0 << 56;// 05 00 00 00 00 00 00 00
        long h1 = b1 << 48;// 00 0A 00 00 00 00 00 00
        long h2 = b2 << 40;
        long h3 = b3 << 32;
        long h4 = b4 << 24;
        long h5 = b5 << 16;
        long h6 = b6 << 8;
        long h7 = b7;
        long h = h0 | h1 | h2 | h3 | h4 | h5 | h6 | h7;
        return Double.longBitsToDouble(h);
    }

    /**
     * 将double转换成字节数组
     *
     * @param value 待转换的double
     * @return 转换之后的字节数组
     */
    public static byte[] doubleToByteArr(double value) {
        long lbits = Double.doubleToLongBits(value);
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (lbits >> 56);
        bytes[1] = (byte) (lbits >> 48);
        bytes[2] = (byte) (lbits >> 40);
        bytes[3] = (byte) (lbits >> 32);
        bytes[4] = (byte) (lbits >> 24);
        bytes[5] = (byte) (lbits >> 16);
        bytes[6] = (byte) (lbits >> 8);
        bytes[7] = (byte) (lbits);
        return bytes;
    }

    /**
     * 将字节数组转化成char
     *
     * @param b 待转换的字节数组
     * @return 转换出来的char
     */
    public static char byteToChar(byte[] b) {
        char c = (char) (((b[1] & 0xFF) << 8) | (b[0] & 0xFF));
        return c;
    }


    /**
     * 将字节数组转换成十六进制字符串
     *
     * @param bytes 待转换的字节数组
     * @return 转换之后的十六进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {

        String result = "";
        String hex = "";
        int F0 = 0xF0;
        int ZeroF = 0x0F;
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf("0123456789ABCDEF".charAt((bytes[i] & F0) >> 4));
            //字节低4位
            hex += String.valueOf("0123456789ABCDEF".charAt(bytes[i] & ZeroF));
            result += hex;
        }
        return result;
    }


    /**
     * 将16进制转换成二进制字节数组,注意16进制不要输入0x，只需输入ff，不要输入成0xff
     *
     * @param hexString 带转换的十六进制字符串
     * @return 转换之后的字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 二进制数组转换为二进制字符串   2-2
     */
    public static String bytesToBinStr(byte[] bArray) {
        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            //高四位
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            //低四位
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    /**
     * 将十六进制字符串转换为二进制字符串
     */
    public static String hexStr2BinStr(String hexString) {
        return bytesToBinStr(hexStringToBytes(hexString));
    }

}