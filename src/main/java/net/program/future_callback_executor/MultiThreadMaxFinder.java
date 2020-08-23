package net.program.future_callback_executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadMaxFinder {

    public int max(int[] arr) throws ExecutionException, InterruptedException {

        int length = arr.length;
        FindMaxTask task1 = new FindMaxTask(arr, 0, length / 2);
        FindMaxTask task2 = new FindMaxTask(arr, length / 2, length);

        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future1 = service.submit(task1);
        Future<Integer> future2 = service.submit(task2);

        service.shutdown();
        return Math.max(future1.get(), future2.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] arr = {1, 100, 20, 50, 50, 102, 90, 80, 99, 201, 901, 809, 827, 99, 902, 102, 9982, 821, 78931, 90432,
                1321, 73428, 7832, 2134, 4324, 78658, 5435, 53534, 5436, 5322, 5345};
        MultiThreadMaxFinder maxFinder = new MultiThreadMaxFinder();
        System.out.println(maxFinder.max(arr));
    }

}
