package io.artoo.lance.cursor.async;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.Task;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

final class AsyncYield<T> implements AsyncCursor<T> {
  private final Task<T> task;
  private final Suppl.Uni<T> callable;
  private final Done done;

  AsyncYield(final Task<T> task, final Suppl.Uni<T> callable) {
    this.task = task;
    this.callable = callable;
    this.done = new Done();
  }


  @Override
  public T fetch() throws Throwable {
    final var future = task.submit(callable);
    done.value = true;

    if (future.isDone()) {
      return future.get();
    } else if (future.isCancelled()) {
      return null;
    } else {
      return future.get(200, MILLISECONDS);
    }
  }

  @Override
  public boolean hasNext() {
    return !done.value;
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  private static final class Done {
    private boolean value = false;
  }
}
