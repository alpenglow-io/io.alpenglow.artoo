package io.artoo.query.one;

import io.artoo.func.Func;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.impl.Select;
import io.artoo.query.one.impl.SelectOne;

import static io.artoo.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (index, it) -> select.apply(it))::iterator;
  }

  default <R, O extends One<R>> One<R> selectOne(final Func<? super T, ? extends O> selectOne) {
    return new SelectOne<>(this, nonNullable(selectOne, "selectOne"))::iterator;
  }

}

