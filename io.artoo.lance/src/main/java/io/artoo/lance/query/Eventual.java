package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Pick;
import io.artoo.lance.cursor.Tick;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.eventual.Awaitable;
import io.artoo.lance.query.eventual.Filterable;
import io.artoo.lance.query.eventual.Peekable;
import io.artoo.lance.query.eventual.Projectable;

public interface Eventual<T> extends Projectable<T>, Filterable<T>, Peekable<T>, Awaitable<T> {
  static <T> Eventual<T> promise(final Suppl.Uni<T> taskable) {
    return new Promise<>(taskable);
  }

  static <T> Eventual<T> aid(T help) {
    return new Aid<>(help);
  }
}

final class Promise<T> implements Eventual<T> {
  private final Suppl.Uni<T> taskable;

  public Promise(final Suppl.Uni<T> taskable) {
    assert taskable != null;
    this.taskable = taskable;
  }

  @Override
  public final Cursor<T> cursor() {
    return Tick.next(taskable);
  }
}

final class Aid<T> implements Eventual<T> {
  private final T help;

  Aid(final T help) {
    assert help != null;
    this.help = help;
  }

  @Override
  public Cursor<T> cursor() throws Throwable {
    return Pick.just(help);
  }
}
