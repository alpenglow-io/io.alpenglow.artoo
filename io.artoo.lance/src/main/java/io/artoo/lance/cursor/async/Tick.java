package io.artoo.lance.cursor.async;

import io.artoo.lance.cursor.Hand;
import io.artoo.lance.func.Suppl;

public final class Tick<T> implements Hand<T> {
  private final Suppl.Uni<T> task;

  public Tick(final Suppl.Uni<T> task) {
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
