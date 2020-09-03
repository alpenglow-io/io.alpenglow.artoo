package io.artoo.lance.query;

import io.artoo.lance.next.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T> {
  static <T> One<T> of(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  static <T> One<T> done(Cursor<T> cursor) {
    return new Done<>(cursor);
  }

  static <L> One<L> none() {
    return new None<>(Cursor.nothing());
  }

  default T yield() {
    return cursor().next();
  }
}

final class Lone<T> implements One<T> {
  private final T element;

  Lone(final T element) {
    assert element != null;
    this.element = element;
  }

  @Override
  public final Cursor<T> cursor() {
    return Cursor.just(element);
  }
}

record None<T>(Cursor<T> cursor) implements One<T> {}

final class Done<T> implements One<T> {
  private final Cursor<T> cursor;

  Done(final Cursor<T> cursor) {this.cursor = cursor;}

  @Override
  public Cursor<T> cursor() {
    final var yielded = cursor.yield();
    T result = null;

    while (yielded.hasNext()) {
      var next = yielded.next();
      if (next != null) result = next;
    }

    return Cursor.maybe(result);
  }
}
