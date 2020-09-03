package io.artoo.lance.cursor.tick;

import io.artoo.lance.cursor.Cursor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class Await<T> extends ForkJoinPool implements Cursor<T> {
  private final RecursiveTask<T> task;
  private volatile T awaited;

  public Await(final RecursiveTask<T> task) {
    super(2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    this.task = task;
  }

  @Override
  public T fetch() throws Throwable {
    execute(task);
    execute(() -> {
      tryComplete:
        if (task.isDone()) {
          awaited = task.getRawResult();
        } else {
          break tryComplete;
        }
    });
    return invoke(task);
  }

  @Override
  public boolean hasNext() {
    return !task.isDone();
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }
}
