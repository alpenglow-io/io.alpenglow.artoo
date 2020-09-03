package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;

import static java.util.concurrent.ForkJoinPool.commonPool;

public interface Awaitable<T> extends Next<T> {
  default Cursor<T> await() {
    return new Await<>(this::fetch);
  }

  default <R> Cursor<R> await(Func.Uni<? super T, ? extends R> as) {
    return new Await<>(this::fetch, as);
  }
}

final class AwaitException extends RuntimeException {
  public AwaitException(final Throwable cause) {
    super(cause);
  }
}

final class Await<T, R> extends FutureTask<T> implements Cursor<R> {
  private final ForkJoinPool pool = commonPool();
  private final Func.Uni<? super T, ? extends R> map;

  @SuppressWarnings("unchecked")
  public Await(final Suppl.Uni<T> callable) {
    this(callable, it -> (R) it);
  }

  public Await(final Suppl.Uni<T> callable, final Func.Uni<? super T, ? extends R> map) {
    super(callable);
    this.map = map;
  }

  {
    pool.execute(this);
  }

  @Override
  protected final void done() {
    try {
      map.tryApply(get());
    } catch (Throwable throwable) {
      throw new AwaitException(throwable);
    }
  }

  @Override
  public final R fetch() throws Throwable {
    return map.tryApply(get());
  }

  @Override
  public final boolean hasNext() {
    return !isDone();
  }
}
