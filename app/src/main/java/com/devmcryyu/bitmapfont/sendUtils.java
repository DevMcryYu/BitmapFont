package com.devmcryyu.bitmapfont;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 92075 on 2018/5/4.
 */

public class sendUtils {
    public static void sendBitMapFontByIP(String ipAddress, int port, byte[][] content) {
        try {
            Socket socket = new Socket(ipAddress, port);
            OutputStream OutputStream = socket.getOutputStream();
            byte[] bytes = new byte[content.length * content[0].length];
            int k = 0;
            for (int i = 0; i < content.length; i++)
                for (int j = 0; j < content[0].length; j++)
                    bytes[k++] = content[i][j];
            Log.i("content", bytes.toString());
//            OutputStream.write(bytes);
//            OutputStream.write(01010);
            OutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendBitMapFontBySocket(Socket socket, byte[][] content) {
        try {
            OutputStream OutputStream = socket.getOutputStream();
            byte[] bytes = new byte[content.length * content[0].length];
            int k = 0;
            for (int i = 0; i < content.length; i++)
                for (int j = 0; j < content[0].length; j++) {
                    bytes[k++] = content[i][j];
                    Log.i("content", k + "   " + Integer.toHexString(bytes[k - 1]));
                }
            Log.i("content", "字数:" + content.length + " 长度:" + content[0].length);
            byte[] result = new byte[bytes.length + 1];
            result[0] = (byte) content.length;

            bytes = reverseArray(bytes);
            for (int i = 1; i < result.length; i++)
                result[i] = bytes[i - 1];
            //OutputStream.write(content.length);
//            OutputStream.write(bytes);
            OutputStream.write(result);
//            OutputStream.write(reverseArray(bytes));
            OutputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(mainActivity.TAG,"发送成功");
    }

    public static void sendReverseBitMapFontBySocket(Socket socket, byte[][] content) {
        try {
            OutputStream OutputStream = socket.getOutputStream();
            byte[] bytes = new byte[content.length * content[0].length];
            int k = 0;
            for (int i = 0; i < content.length; i++)
                for (int j = 0; j < content[0].length; j++) {
                    bytes[k++] = content[i][j];
                    Log.i("content", k + "   " + Integer.toHexString(bytes[k - 1]));
                }
            Log.i("content", "字数:" + content.length + " 长度:" + content[0].length);
            byte[] result = new byte[bytes.length + 1];
            result[0] = (byte) content.length;
            bytes = reverseArray(bytes);
            for (int i = 1; i < result.length; i++)
                result[i] = bytes[i - 1];
            //OutputStream.write(content.length);
//            OutputStream.write(bytes);
            OutputStream.write(reverseArray(result));
            OutputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendBitMapFontBySocket(Socket socket, String content) {
        try {
            OutputStream OutputStream = socket.getOutputStream();
            OutputStream.write(content.getBytes("GBK"));
            OutputStream.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] reverseArray(byte[] Array) {
        byte[] new_array = new byte[Array.length];
        for (int i = 0; i < Array.length; i++) {
            // 反转后数组的第一个元素等于源数组的最后一个元素：
            new_array[i] = Array[Array.length - i - 1];
        }
        return new_array;
    }
}
