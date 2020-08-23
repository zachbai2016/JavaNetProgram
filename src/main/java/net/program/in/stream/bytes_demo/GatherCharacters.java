package net.program.in.stream.bytes_demo;

import java.io.IOException;
import java.io.InputStream;

public class GatherCharacters {


    static void gatherCharacters(InputStream in) throws IOException {

        int bytesRead = 0;
        int bytesToRead = 1024;
        byte[] input = new byte[bytesToRead];

        while (bytesRead < bytesToRead) {

            int result = in.read(input, bytesRead, bytesToRead - bytesRead);// 注意第三个参数
            if (result == -1) {
                break;
            }
            bytesRead += result;
        }

    }

    static void gatherCharactersAvailable(InputStream in) throws IOException {
        int bytesAvailable = in.available();
        byte[] input = new byte[bytesAvailable];
        int readBytes = in.read(input, 0, bytesAvailable);  // 可用的len = 0 时， 会返回0， 即使没有数据可读 也只是返回0 而 不是 -1
        // 而 len != 0 时，   此时若没有数据刻度， 则会返回-1。
        // skip 跳过一些数据 不做读取
        // 文件时RandomAccess访问的，所以skip就是重新修改指向正在读的指针，而不用遍历要跳过的每一个字节。
        // close 会关闭一些资源比如 释放句柄 或 关闭connection 或 channel

        // 标记 重置
        // markSupport() 是否支持mark 以及 mark只能在一个inputStream中一次， 多次会覆盖。 太远会IOE
        // 父类的抽象对自类的实现不友好，这种设计是有问题的。

        // mark reset markSupport() 放在一个Interface中， 由extends InputStream的类来决定是否 implements 这个Interface 的这种设计，比将
        // 这三个方法放在InputStream中的 这种设计更友好。

        // ByteArrayInputStream BufferedInputStream 才支持
        // 其他的InputStream 只有串链(chain？)到缓冲区的InputStream时才持支。？


    }

}
