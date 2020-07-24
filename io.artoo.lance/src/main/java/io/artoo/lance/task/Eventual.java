package io.artoo.lance.task;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.async.AsyncCursor;
import io.artoo.lance.task.eventual.Awaitable;
import io.artoo.lance.task.eventual.Projectable;

public interface Eventual<T> extends Projectable<T>, Awaitable<T> {
  static <T> Eventual<T> promise(final Suppl.Uni<T> callable) {
    return new Promise<>(callable);
  }
}

final class Promise<T> implements Eventual<T> {
  private final Suppl.Uni<T> callable;

  public Promise(final Suppl.Uni<T> callable) {
    assert callable != null;
    this.callable = callable;
  }

  @Override
  public final Cursor<T> cursor() {
    return AsyncCursor.just(callable);
  }
}
