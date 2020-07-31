package io.artoo.lance.cursor;

import io.artoo.lance.cursor.pick.Concat;
import io.artoo.lance.cursor.pick.Exceptionally;
import io.artoo.lance.cursor.pick.Flat;
import io.artoo.lance.cursor.pick.Map;
import io.artoo.lance.cursor.pick.Yield;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Cursor<T> extends Iterator<T> {
  T fetch() throws Throwable;

  default Cursor<T> order() throws Throwable {
    return this;
  }

  default <P> Cursor<P> map(Func.Uni<? super T, ? extends P> map) {
    return new Map<>(this, nonNullable(map, "map"));
  }

  default <P, C extends Cursor<P>> Cursor<P> flatMap(Func.Uni<? super T, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, nonNullable(flatMap, "flatMap")));
  }

  default Cursor<T> concat(final Cursor<T> cursor) {
    return new Concat<>(Cursors.enqueue(this, cursor));
  }

  default Cursor<T> exceptionally(final Cons.Uni<? super Throwable> catch$) {
    return new Exceptionally<>(this, catch$);
  }

  default Cursor<T> yield() {
    return new Yield<>(this);
  }

  default <C extends Cursor<T>> Cursor<T> or(final Suppl.Uni<? extends C> otherwise) {
    return this.hasNext() ? this : otherwise.get();
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    if (this.hasNext()) {
      return this;
    } else {
      throw exception.apply(message);
    }
  }
}


