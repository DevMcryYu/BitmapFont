package com.devmcryyu.bitmapfont;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by 92075 on 2018/1/22.
 * 将字符转换成对应的点阵字符
 * HZK16字库里的16×16汉字一共需要256个点来显示，也就是说需要32个字节才能达到显示一个普通汉字的目的。
 * 我们知道一个GB2312汉字是由两个字节编码的，范围为0xA1A1~0xFEFE。A1-A9为符号区，B0-F7为汉字区。每一个区有94个字符
 * (注意:这只是编码的许可范围，不一定都有字型对应，比如符号区就有很多编码空白区域)。
 * 下面以汉字"我"为例，介绍如何在HZK16文件中找到它对应的32个字节的字模数据。前面说到一个汉字占两个字节，这两个中前一
 * 个字节为该汉字的区号，后一个字节为该字的位号。其中，每个区记录94个汉字，位号为该字在该区中的位置。所以要找到"我"
 * 在hzk16库中的位置就必须得到它的区码和位码。
 * 区码:汉字的第一个字节-0xA0 (因为汉字编码是从0xA0区开始的， 所以文件最前面就是从0xA0区开始， 要算出相对区码)
 * 位码:汉字的第二个字节-0xA0
 * 这样我们就可以得到汉字在HZK16中的绝对偏移位置:
 * offset=(94*(区码-1)+(位码-1))*32
 * 注解:
 * <p>
 * 区码减1是因为数组是以0为开始而区号位号是以1为开始的
 * (94*(区号-1)+位号-1)是一个汉字字模占用的字节数
 * 最后乘以32是因为汉字库文应从该位置起的32字节信息记录该字的字模信息(前面提到一个汉字要有32个字节显示)
 */

public class bitmapFont {
    public Context context;
    public int wordNumber;
    public byte[][] wordsMatrix;
    public static final String TAG = "err";
    public static final int ASC16 = 16;
    public static final int HZK16 = 32;

    public bitmapFont(Context context) {
        this.context = context;
    }

    public byte[][] toBitmapFont(String text) {
        byte[] bytes = null;
        /* getBytes()
          将字符串包含的字符数据转换成byte类型并存入一个byte[]数组
          编码选用GBK编码
          GBK的长度为2, UTF-8的长度为3, ISO8859-1的长度为1。
         */
        try {
            bytes = text.getBytes("GBK");       //获取字节编码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int[] code = new int[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            code[i] = bytes[i] < 0 ? 256 + bytes[i] : bytes[i];
        }
        wordNumber = code.length / 2;
//        wordsMatrix = new byte[HZK16 / 2][HZK16 / 2 * wordNumber];
        byte[][] data = new byte[wordNumber][];
        for (int i = 0; i < wordNumber; i++) {
            // 通过区码和位码获取字库中对应的字模信息
            data[i]=(read(context, code[2 * i], code[2 * i + 1]));

//            for (int j = 0; j < HZK16; j++) {
//                for (int k = 0; k < 8; k++) {
//                    // 将字模信息转化为Boolean类型的二维数组并且进行纵向填充数组
//                    int row = (j * 8 + k) / 16 + i * HZK16 / 2;
//                    int col = (j * 8 + k) % 16;
//                    if (((temp[j] >> (7 - k)) & 1) == 1) {
//                        wordsMatrix[col][row] = 1;
//                    } else {
//                        wordsMatrix[col][row] = 0;
//                    }
//                }
//            }
        }
//        return wordsMatrix;
        return data;
    }

    /**
     * 从字库中获取指定区码和位码汉字的字模信息
     *
     * @param areaCode 区码，对应编码的第一个字节
     * @param posCode  位码，对应编码的第二个字节
     * @return
     */
    private byte[] read(Context context, int areaCode, int posCode) {
        byte[] data = null;
        try {
            int area = areaCode - 0xa0;
            int pos = posCode - 0xa0;
            InputStream in = context.getAssets().open("HZK16");
            int offset = ((area - 1) * 94 + pos - 1) * HZK16;
            in.skip(offset);
            data = new byte[HZK16];
            in.read(data, 0, HZK16);
            in.close();
        } catch (IOException e) {
            Log.d(TAG, "IOException e = " + e.getMessage());
        }
        return data;
    }

}

