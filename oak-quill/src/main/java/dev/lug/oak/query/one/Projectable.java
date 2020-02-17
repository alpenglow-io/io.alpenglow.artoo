package dev.lug.oak.query.one;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.func.Fun;
import dev.lug.oak.query.Queryable;

import static dev.lug.oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Fun<T, R> {}
  interface SelectOne<T, R, O extends One<R>> extends Fun<T, O> {}

  static <T, R> Select<T, R> as(Select<T, R> select) {
    return select;
  }

  static <T, R, O extends One<R>> SelectOne<T, R, O> one(SelectOne<T, R, O> selectOne) {
    return selectOne;
  }

  default <R> One<R> select(final Select<? super T, ? extends R> select) {
    return nonNullable(select, m -> () -> {
      final var value = this.iterator().next();
      return nonNull(value) ? Cursor.ofNullable(select.apply(value)) : Cursor.none();
    });
  }

  default <R, O extends One<? extends R>> One<R> select(final SelectOne<? super T, ? extends R, O> selectOne) {
    return nonNullable(selectOne, f -> () -> select(selectOne).iterator());
  }
}

