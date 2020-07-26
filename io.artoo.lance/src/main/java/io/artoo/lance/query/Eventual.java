package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Hand;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.eventual.Awaitable;
import io.artoo.lance.query.eventual.Projectable;

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
    return Hand.tick(callable);
  }
}
