package net.program.filter;

import java.io.*;

public class FilterChainDemo {

}


class ReadFilterChain {
    void readFilterChain() throws IOException {
        FileInputStream fis = new FileInputStream("D://a.txt");
        BufferedInputStream bis = new BufferedInputStream(fis);
        // 有时担心对于同一个数据源 获取两个不同的流 调用 write 或 read方法
        // 会这样写
        InputStream in = new FileInputStream("D:\\a.txt");
        in = new BufferedInputStream(in);
    }
}