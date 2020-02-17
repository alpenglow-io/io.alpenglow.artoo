package dev.lug.oak.query.one;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Pre;
import dev.lug.oak.query.Queryable;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(Pre<? super T> filter) {
    return nonNullable(filter, f -> () -> {
      final var value = this.iterator().next();
      return nonNull(value) && filter.apply(value) ? Cursor.all(value) : Cursor.none();
    });
  }

  default <R> One<R> ofType(Class<? extends R> type) {
    return nonNullable(type, t -> () -> {
      final var value = this.iterator().next();
      return nonNull(value) && t.isInstance(value) ? Cursor.all(t.cast(value)) : Cursor.none();
    });
  }
}
