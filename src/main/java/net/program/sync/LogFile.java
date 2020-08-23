package net.program.sync;

import java.io.*;
import java.util.Date;

/**
 * 两个线程共同写一个文件
 */
public class LogFile {

    private Writer out;

    public LogFile(File f) throws IOException {
        FileWriter writer = new FileWriter(f);
        this.out = new BufferedWriter(writer);
    }

    //Entry=1999-12-26 09:12:29    ${message}
    public synchronized void writeEntry(String message) throws IOException {
        Date date = new Date();
        out.write(date.toString());
        out.write("\t");
        out.write(message);
        out.write("\r\n");
    }

    public synchronized void close() throws IOException {
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        LogFile logFile = new LogFile(new File("./bWithSync.log"));
        new Thread(new LogFileARunnable(logFile)).start();
        new Thread(new LogFileBRunnable(logFile)).start();
    }
}

class LogFileARunnable implements Runnable {
    LogFile logFile;

    public LogFileARunnable(LogFile logFile) {
        this.logFile = logFile;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                logFile.writeEntry("A+" + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 两个线程共享同一个文件
 */
class LogFileBRunnable implements Runnable {
    LogFile logFile;

    public LogFileBRunnable(LogFile logFile) {
        this.logFile = logFile;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                logFile.writeEntry("B+" + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                logFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}