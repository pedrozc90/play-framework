package actors.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TaskExecutor {

    private static TaskExecutor instance;

    private final ForkJoinPool pool;

    public TaskExecutor() {
        final int cores = Math.max(Runtime.getRuntime().availableProcessors(), 1);
        this.pool = new ForkJoinPool(cores);
    }

    public static TaskExecutor getInstance() {
        if (instance == null) {
            instance = new TaskExecutor();
        }
        return instance;
    }

    public <T> List<T> execute(final List<RecursiveTask<T>> tasks) {
        final List<T> results = new ArrayList<>();

        for (final RecursiveTask<T> task : tasks) {
            pool.submit(task);
        }

        for (final RecursiveTask<T> task : tasks) {
            final T result = task.join();
            results.add(result);
        }

        return results;
    }

    public <T> T execute(final RecursiveTask<T> task) {
        return pool.invoke(task);
    }

}
