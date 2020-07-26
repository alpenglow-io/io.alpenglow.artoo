package io.artoo.lance.cursor.async;

import io.artoo.lance.cursor.Hand;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.Task;

import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@SuppressWarnings("StatementWithEmptyBody")
public final class Await<T> implements Hand<T> {
  private final Task<T> task;
  private final Suppl.Uni<T> call;
  private final int timeout;
  private final Has has;

  public Await(final Task<T> task, final Suppl.Uni<T> call) {
    this(task, call, 500);
  }
  public Await(final Task<T> task, final Suppl.Uni<T> call, final int timeout) {
    this.task = task;
    this.call = call;
    this.timeout = timeout;
    this.has = new Has();
  }


  @Override
  public T fetch() throws Throwable {
    if (has.future.isDone()) {
      has.result = true;
      return has.future.get();
    } else if (has.future.isCancelled()) {
      has.result = true;
      return null;
    } else {
      has.result = true;
      return has.future.get(timeout, MILLISECONDS);
    }
  }

  @Override
  public boolean hasNext() {
    has.future = task.commit(call);
    return !has.result && (has.future.isDone() || !has.future.isCancelled());
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  private final class Has {
    private Future<T> future;
    private boolean result = false;
  }
}
