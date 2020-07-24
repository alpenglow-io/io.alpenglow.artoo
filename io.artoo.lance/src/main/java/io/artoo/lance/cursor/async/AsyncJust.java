package io.artoo.lance.cursor.async;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.task.Eventual;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

final class AsyncJust<T> implements AsyncCursor<T> {
  private final Suppl.Uni<T> task;

  AsyncJust(final Suppl.Uni<T> task) {
    assert task != null;
    this.task = task;
  }

  @Override
  public T fetch() throws Throwable {
    return task.tryGet();
  }

  @Override
  public boolean hasNext() {
    return true;
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
