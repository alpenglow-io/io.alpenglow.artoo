package io.artoo.lance.task;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Pick;
import io.artoo.lance.cursor.Tick;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.eventual.Filterable;
import io.artoo.lance.task.eventual.Peekable;
import io.artoo.lance.task.eventual.Projectable;

public interface Eventual<T> extends Projectable<T>, Filterable<T>, Peekable<T> {
}

final class Closing<T> implements Eventual<T> {
  private final Suppl.Uni<T> taskable;

  public Closing(final Suppl.Uni<T> taskable) {
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
