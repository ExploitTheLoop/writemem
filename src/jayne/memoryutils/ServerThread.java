package jayne.memoryutils;

import java.io.*;
import java.net.Socket;


public class ServerThread extends Thread {

    private Socket socket = null;

    private Object bytes = new Object();

    private boolean state = true;

    public void setState(boolean state) {
        this.state = state;
    }

    public void setBytes(Object bytes) {
        this.bytes = bytes;
    }

    public boolean isState() {
        return state;
    }

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream os = null;
        ObjectOutputStream bos = null;
        try {
            os = socket.getOutputStream();
            bos = new ObjectOutputStream(os);
            while (state){
                bos.writeObject(bytes);
                bos.flush();
            }
        } catch (Exception e) {
            System.err.println("数据传输中出现错误!");
            e.printStackTrace();
            // TODO: handle exception
        } finally {
            //关闭资源
            try {
                if (bos != null)
                    bos.close();
                if (os != null)
                    os.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}