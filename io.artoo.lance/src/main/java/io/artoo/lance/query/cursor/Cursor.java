package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Cursor<R> extends Iterator<R> {
  R fetch() throws Throwable;

  default <P> P fetch(final Func.Uni<R, P> then) throws Throwable {
    return then.tryApply(fetch());
  }

  default <P> Cursor<P> map(Func.Uni<? super R, ? extends P> map) {
    return new Map<>(this, nonNullable(map, "map"));
  }

  default <P, C extends Cursor<P>> Cursor<P> flatMap(Func.Uni<? super R, ? extends C> flatMap) {
    return new Flat<>(new Map<>(this, nonNullable(flatMap, "flatMap")));
  }

  default Cursor<R> close() throws Throwable {
    R element = null;
    while (hasNext()) element = fetch();
    return Cursor.readonly(element);
  }

  default Cursor<R> concat(final Cursor<R> cursor) {
    return new Concat<>(this, nonNullable(cursor, "cursor"));
  }

  default <C extends Cursor<R>> Cursor<R> or(final Suppl.Uni<? extends C> alternative) {
    return this.hasNext() ? this : alternative.get();
  }

  default <E extends RuntimeException> Cursor<R> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    if (this.hasNext()) {
      return this;
    } else {
      throw exception.apply(message);
    }
  }

  @SafeVarargs
  static <R> Cursor<R> readonly(final R... elements) {
    return new Readonly<>(nonNullable(elements, "elements"));
  }
}

