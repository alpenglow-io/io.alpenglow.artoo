package io.artoo.lance.query;

import io.artoo.lance.next.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
    return new None<>();
  }

  @Deprecated(forRemoval = true)
  default T yield() {
    return iterator().next();
  }

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return cursor().close();
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

final class None<T> implements One<T> {
  @Override
  public final Cursor<T> cursor() {
    return Cursor.nothing();
  }
}

record Done<T>(Cursor<T> cursor) implements One<T> {}
}
