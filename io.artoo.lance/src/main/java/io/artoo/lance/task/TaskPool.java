package io.artoo.lance.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public interface TaskPool<T> {
  static <T> TaskPool<T> returning() {
    return new ReturningTaskPool<>();
  }

  void commit(RecursiveTask<T> task);
}

final class ReturningTaskPool<T> extends ForkJoinPool implements TaskPool<T> {
  @Override
  public void commit(RecursiveTask<T> task) {
    this.execute(task);
  }
}
