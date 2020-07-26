package io.artoo.lance.cursor;

import io.artoo.lance.cursor.sync.Concat;
import io.artoo.lance.cursor.sync.Every;
import io.artoo.lance.cursor.sync.Exceptionally;
import io.artoo.lance.cursor.sync.Flat;
import io.artoo.lance.cursor.sync.Iteration;
import io.artoo.lance.cursor.sync.Just;
import io.artoo.lance.cursor.sync.Map;
import io.artoo.lance.cursor.sync.Nothing;
import io.artoo.lance.cursor.sync.Yield;
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

  default Cursor<R> yield() {
    return new Yield<>(this);
  }

  default Cursor<R> order() throws Throwable {
    return this;
  }

  default Cursor<R> concat(final Cursor<R> cursor) {
    return new Concat<>(Cursors.enqueue(this, cursor));
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

  default Hand<R> asHand() {
    return this instanceof Hand<R> h ? h : Hand.of(this);
  }

  @SafeVarargs
  static <R> Cursor<R> every(final R... elements) {
    return new Every<>(nonNullable(elements, "elements"));
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
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

