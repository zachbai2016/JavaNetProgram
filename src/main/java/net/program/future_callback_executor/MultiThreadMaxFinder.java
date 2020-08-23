package net.program.future_callback_executor;

import java.util.concurrent.*;

public class MultiThreadMaxFinder {

    //
    public int max(int[] arr) throws ExecutionException, InterruptedException {

        // step1. 对数据进行分割
        int length = arr.length;
        FindMaxTask task1 = new FindMaxTask(arr, 0, length / 2);
        FindMaxTask task2 = new FindMaxTask(arr, length / 2, length);

        // step2. 将一定的数据量绑定一个小任务上 开启多个小任务
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future1 = service.submit(task1);
        Future<Integer> future2 = service.submit(task2);

        // 等待10秒 要么时间先到 要么先完成
        service.awaitTermination(10, TimeUnit.SECONDS);

        // 关闭线程池 对已经提交的任务无影响
        service.shutdown();

        // step3. 等带小任务运行完成，进行结果汇总
        return Math.max(future1.get(), future2.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] arr = {1, 100, 20, 50, 50, 102, 90, 80, 99, 201, 901, 809, 827, 99, 902, 102, 9982, 821, 78931, 90432,
                1321, 73428, 7832, 2134, 4324, 78658, 5435, 53534, 5436, 5322, 5345};
        // 大任务： 寻找这么多数中最大的数字
        MultiThreadMaxFinder maxFinder = new MultiThreadMaxFinder();
        System.out.println(maxFinder.max(arr));
    }

}
