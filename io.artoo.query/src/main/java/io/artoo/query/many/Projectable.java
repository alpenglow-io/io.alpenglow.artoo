package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.impl.Select;
import io.artoo.query.impl.SelectMany;

import java.util.function.BiFunction;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;

public interface Projectable<T extends Record> extends Queryable<T> {
  default <R extends Record> Many<R> select(BiFunction<? super Integer, ? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (i, it) -> {}, select)::iterator;
  }

  default <R extends Record> Many<R> select(Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select((index, value) -> select.apply(value));
  }

  default <R extends Record, M extends Many<R>> Many<R> selectMany(BiFunction<? super Integer, ? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (i, it) -> {}, select)::iterator;
  }

  default <R extends Record, M extends Many<R>> Many<R> selectMany(Function<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (i, it) -> {}, (index, it) -> select.apply(it))::iterator;
  }
}

