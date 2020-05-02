package io.artoo.query.one;


import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.impl.Select;
import io.artoo.query.one.impl.SelectOne;

import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;

public interface Projectable<T extends Record> extends Queryable<T> {
  default <R extends Record> One<R> select(final Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (i, it) -> {}, (index, it) -> select.apply(it))::iterator;
  }

  default <R extends Record, O extends One<R>> One<R> selectOne(final Function<? super T, ? extends O> selectOne) {
    return new SelectOne<>(this, nonNullable(selectOne, "selectOne"))::iterator;
  }

}

