package net.program.future_callback_executor;

import java.util.concurrent.Callable;

/**
 * 通过将一个大任务 分割成小的可接受时间内完成的小任务
 * 将这些小任务提交到线程池，
 * 最后，将这些任务运行的结果进行汇总
 */
// 这个FindMaxTask 就是代表着最小的任务
public class FindMaxTask implements Callable<Integer> {

    private int[] arr;
    private int start;
    private int end;

    public FindMaxTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    public Integer call() throws Exception {
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }
}
