数据流
    DataInputStream
    DataOutputStream

使用 BufferedReader 来readLine
     * <pre>
     * BufferedReader in
     *   = new BufferedReader(new FileReader("foo.in"));
     * </pre>
     *
     * will buffer the input from the specified file.  Without buffering, each
     * invocation of read() or readLine() could cause bytes to be read from the
     * file, converted into characters, and then returned, which can be very
     * inefficient.
     BufferedReader
        尽可能多的批量读取数据到buffer中，批量做编码转换，而不是每次都单独执行，效率低下。
    BufferedWriter
        尽可能多的批量写入数据到buffer中，批量做编码转换，而不是每次都单独执行，效率低下。


Readable：
    针对字符数组可读的接口

面向字符：
    Reader              抽象的字符流读取器
    Writer              抽象的字符流写入器
    InputStreamReader(重要)   根据不同的编码将字节流读取为字符流 会根据不同的编码构建 StreamDecoder
    OutputStreamWriter(重要)  根据不同的编码将字符流写入为字符流 会根据不同的编码构建 StreamEncoder

面向文件的字节流读取：
    FileInputStream     面向字节流读取文件(或文件描述符) 通过字节流读取文件   不适合字符文件的读取  适合例如图片 音频 视频文件的读取
    FileOutputStream    面向字节流写入文件(或文件描述符) 通过字节流写入文件  不适合字符文件的写入  适合例如图片 音频 视频文件的写入

面向字符流的文件读写器：
     FileReader     FileReader is meant for reading streams of characters. For reading streams of raw bytes, consider using a FileInputStream.
                    FileReader 通过父类 InputStreamReader 传入 FileInputStream 来构建，FileInputStream 本身 继承了InputStream
                    FileReader 通过父类 InputStreamReader 传入 FileInputStream 来构建， 同时构建了 StreamDecoder 通过解码器将读到的字节流中的数据转化为字符再返回
                    我们读取的字节数组，通过解码器先转换成字符数组，再返回。
     FileWriter     FileWriter is meant for writing streams of characters. For writing streams of raw bytes, consider using a FileOutputStream.
                    FileWriter 通过父类 OutputStreamWriter 传入 FileOutputStream 来构建， 同时构建了 StreamEncoder 通过编码器将写入的字符数据转化为字节数据再写入
                    我们写入的字符数组，通过编码器先转换成字节数组，再写入。


UTF-8
    一个英文字符占用一个字节

UTF-16
    一个英文字符占用两个字节    涉及到大端、小端表示法

包装类的close() 方法会执行flush操作 将数据刷写出去 然后关闭相应的资源 如channel encoder 等资源的关闭 最后关闭stream。



网络编程中勿用此方法：
    1. java.io.BufferedWriter.newLine 各平台的分隔符不同 unix \n  windonws \r\n 有坑
    2. 网络协议一般会指定所需要的行结束符，大多数情况下是\r\n成对出现
    3. PrintWriter仍然不适用于网络编程
    4. IO是个细活




chap02:

    1. 线程
        1. 线程池可以避免频繁的创建和销毁线程
        2. 带缓冲的读写确实更快，因为单次的读取或写入是多字节而不是单字节
        3. 从线程返回需要的信息
        4. sleep 是线程的方法     sleep不会释放锁  sleep方法不依赖于同步器synchronized  sleep不需要被唤醒（休眠之后退出阻塞）
        5. wait  是Object的方法   wait会释放锁
        6. join方法是如何被唤醒的？ 涉及到ThreadGroup 树结构。


        Thread#exit:
        /**
         * This method is called by the system to give a Thread
         * a chance to clean up before it actually exits.
         */
        private void exit() {
            if (group != null) {
                group.threadTerminated(this);
                group = null;
            }
            /* Aggressively null out all reference fields: see bug 4006245 */
            target = null;
            /* Speed the release of some of these resources */
            threadLocals = null;
            inheritableThreadLocals = null;
            inheritedAccessControlContext = null;
            blocker = null;
            uncaughtExceptionHandler = null;
        }


        Thread#threadTerminated
        /**
         * Notifies the group that the thread {@code t} has terminated.
         *
         * <p> Destroy the group if all of the following conditions are
         * true: this is a daemon thread group; there are no more alive
         * or unstarted threads in the group; there are no subgroups in
         * this thread group.
         *
         * @param  t
         *         the Thread that has terminated
         */
        void threadTerminated(Thread t) {}


        Object:
             * Wakes up all threads that are waiting on this object's monitor. A
             * thread waits on an object's monitor by calling one of the
             * {@code wait} methods.
             * <p>
             * The awakened threads will not be able to proceed until the current
             * thread relinquishes the lock on this object. The awakened threads
             * will compete in the usual manner with any other threads that might
             * be actively competing to synchronize on this object; for example,
             * the awakened threads enjoy no reliable privilege or disadvantage in
             * being the next thread to lock this object.
             * <p>
             * This method should only be called by a thread that is the owner
             * of this object's monitor. See the {@code notify} method for a
             * description of the ways in which a thread can become the owner of
             * a monitor.

        7. 通过使用Object 的 wait notify 实现了阻塞式获取计算结果的返回值
        8. 如果是主线程轮询式的获取计算结果，而不采用wait notify 机制，可能因为主线程一直轮询而计算线程得不到CPU的调度，长时间内无法得到计算结果
        9. 另外，轮询的串行获取实在太慢了，只能在第一个获取到之后才能获取第二个，否则即使第二个已经计算出来了也无法获取到结果

引出回调callback：

        1. 通过主线程中的方法，让计算线程去回调而做到异步结果的获取
        2. 回调释放了主线程因为一直等待计算结果而无法计算的这段时间，让主线程可以更高效的做其他事情
        3. 实例回调的优点：
            不会浪费CPU
            回调使程序变得更灵活 可以做更多更复杂的事情
            这里引出了 观察者设计模式
        4. 批量回调 先将对计算结果感兴趣的对象添加到工作线程的列表中，等到计算结果完成后再批量回调各实例的回调方法 可通过多态实现

Future Executor Callback

        1. 关于线程池 executor 看java并发编程的艺术
        
同步 -> 不可变 -> 可变对象作为一个私有成员变量
原子类 AtomicX -> 包装原子类 AtomicReference (使用这个类不是说 成员变量就变成线程安全的了， 而是这个类的成员变量的get和set是线程安全的。)
同步集合 Collections.synchronizedXXX();        

死锁
    同步是确保线程安全的最后一道防线。
    如果确实需要，应该确保范围足够小。因为java类库的底层也可能会同步一些对象。
    
线程调度
    饥饿线程如何避免？sleep吗？
    
抢占 - preempt
    调度器：
        1. 抢占式  强制释放cpu使用权
        2. 协作式  自己释放cpu使用权，更容易使被等待的线程陷入饥饿
    
    10种让线程让出cpu使用权的方法
        1. IO block 等待资源时，就会发生阻塞。自动放弃CPU使用权，在阻塞的时候可以放弃。阻塞几秒，足够CPU运行其他线程很多任务。
        2. synchronized object block 线程在进入一个同步方法 或 代码块时 也会阻塞。
        不论io阻塞还是对锁的阻塞，都不会释放线程已经拥有的锁。
        IO阻塞：1. 要么等到了数据的到来， 2. 要么抛出异常 最终IO都不会阻塞而继续正常运行，最后退出同步方法或同步块，释放锁。
        
        但是，如果一个线程如果因为没有获得一把锁而阻塞，它将永远不会放弃已经拥有的锁。
        如果a在等待b拥有的锁，b同时也在等待a拥有的锁，就会导致死锁。
        如果发生了死锁， 该这么办？jstat?
        
        3. sleep
        4. yeild
            显示放弃控制权。通知虚拟机，如果有另一个线程准备运行，看与运行另外的线程。
            yield不会释放锁，所以如果要使用yield方法，不应该在同步方法种使用。
            a 拿到了一把锁lock1， 然后在持有这个锁的时候，调用了 yield方法，
            此时 b c d 也需要争抢这把lock1， 但是 b c d都无法争抢成功， 因为lock1 被已经放弃控制权的 a 锁持有，
            b c d 也因此而拿不到锁，这也失去了 a 放弃控制权的意义。
            while(true) {
                // doSomeThing
                Thread.yield(); // 一般一次就足够了， 因为没有完成任务的情况下， 放弃调用不明显
            }
        5. 



