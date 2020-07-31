package io.artoo.lance.query;

import io.artoo.lance.cursor.Pick;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.cursor.Cursor;
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

  static <L> One<L> done(final Suppl.Uni<Cursor<L>> cursor) {
    return new Done<>(cursor);
  }

  static <L> One<L> none() {
    return new None<>(Pick.nothing());
  }

  default T yield() {
    return iterator().next();
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
    return Pick.just(element);
  }
}

record None<T>(Cursor<T> cursor) implements One<T> {}

@SuppressWarnings({"StatementWithEmptyBody"})
final class Done<T> implements One<T> {
  private final Suppl.Uni<Cursor<T>> cursor;

  Done(final Suppl.Uni<Cursor<T>> cursor) {
    assert cursor != null;
    this.cursor = cursor;
  }

  @Override
  public final Cursor<T> cursor() throws Throwable {
    final var result = cursor.tryGet().yield();
    var element = result.next();
    for (; result.hasNext(); element = result.next());
    return Pick.maybe(element);
  }
}



