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
    return cursor().yield().next();
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

enum Default implements One<Object> {
  None;

  @Override
  public final Cursor<Object> cursor() {
    return io.artoo.lance.next.Cursor.nothing();
  }
}

record None<T>(Cursor<T> cursor) implements One<T> {}

record Done<T>(Cursor<T> cursor) implements One<T> {}
