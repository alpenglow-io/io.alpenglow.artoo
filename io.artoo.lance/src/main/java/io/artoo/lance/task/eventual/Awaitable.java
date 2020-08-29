package io.artoo.lance.task.eventual;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.Eventual;
import io.artoo.lance.task.Operable;
import io.artoo.lance.task.Taskable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;

import static java.util.concurrent.ForkJoinPool.commonPool;

public interface Awaitable<T> extends Taskable<T> {
  default Eventual<T> await() {
/*    final Func.Uni<T, One<T>> later = One::of;
    final var task = Task.returning(() -> cursor().fetch());
    final var pool = commonPool();
    pool.execute(task);
    pool.execute(() -> {
      while (!task.isDone())
        ;
      later.apply(task.getRawResult());
    });*/
    return new Await<>(commonPool(), this.task()).commit();
  }
}

final class Await<T> extends FutureTask<T> implements Operable<T, T> {
  private final ForkJoinPool pool;

  public Await(final ForkJoinPool pool, @NotNull final Callable<T> callable) {
    super(callable);
    this.pool = pool;
  }

  Eventual<T> commit()  {
    pool.execute(this);
    return this;
  }

  @Override
  protected void done() {
    try {
      this.apply(get());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public T tryApply(final T result) {
    return result;
  }
}
