package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public interface TaskPool<T> {
  static <T> TaskPool<T> returns(final Suppl.Uni<T> taskable) {
    return new ReturningTaskPool<>(Task.returns(taskable));
  }

  T commit();
}

final class ReturningTaskPool<T> extends ForkJoinPool implements TaskPool<T> {
  private final RecursiveTask<T> task;

  ReturningTaskPool(final RecursiveTask<T> task) {this.task = task;}

  @Override
  public T commit() {
    return this.invoke(task);
  }
}
