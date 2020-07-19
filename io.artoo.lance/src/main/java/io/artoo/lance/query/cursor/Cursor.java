package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Cursor<R> extends Iterator<R> {
  R fetch() throws Throwable;

  default <P> Cursor<P> map(Func.Uni<? super R, ? extends P> map) {
    return new Map<>(this, nonNullable(map, "map"));
  }

  default <P, C extends Cursor<P>> Cursor<P> flatMap(Func.Uni<? super R, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, nonNullable(flatMap, "flatMap")));
  }

  default Cursor<R> shrink() {
    return new Shrink<>(this);
  }

  default Cursor<R> order() throws Throwable {
    return null;
  }

  default Cursor<R> fastForward() {
    R element = null;
    while (hasNext())
      element = next();

    return Cursor.maybe(element);
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }

  default Cursor<R> concat(final Cursor<R> cursor) {
    return new Concat<>(Cursors.queued(this, cursor));
  }

  default <C extends Cursor<R>> Cursor<R> or(final Suppl.Uni<? extends C> otherwise) {
    return this.hasNext() ? this : otherwise.get();
  }

  default <E extends RuntimeException> Cursor<R> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    if (this.hasNext()) {
      return this;
    } else {
      throw exception.apply(message);
    }
  }

  default Cursor<R> exceptionally(final Cons.Uni<? super Throwable> catch$) {
    return new Exceptionally<>(this, catch$);
  }

  @SafeVarargs
  static <R> Cursor<R> every(final R... elements) {
    return new Every<>(nonNullable(elements, "elements"));
  }

  static <R> Cursor<R> just(final R element) {
    return new Just<>(nonNullable(element, "element"));
  }

  static <R> Cursor<R> nothing() {
    return new Nothing<>();
  }

  static <R> Cursor<R> maybe(final R element) {
    return element == null ? nothing() : just(element);
  }
}

