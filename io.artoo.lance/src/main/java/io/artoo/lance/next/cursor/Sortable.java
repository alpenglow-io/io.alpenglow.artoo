package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;

import java.util.Arrays;

public interface Sortable<T> extends Projectable<T> {
  default Cursor<T> order() {
    return Cursor.later(new Order<>(this));
  }
}

final class Order<T> implements Suppl.Uni<Next<T>> {
  private final Next<T> next;

  Order(final Next<T> next) {this.next = next;}

  @Override
  public Next<T> tryGet() throws Throwable {
    Arrays.sort(next.fetchAll());
    return next;
  }
}
