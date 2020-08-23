package net.program.future_callback_executor;

import java.util.concurrent.Callable;

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
