package oak.query.one;

import oak.func.Func;
import oak.query.One;
import oak.query.Queryable;
import oak.query.internal.Select;
import oak.query.internal.SelectQueryable;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (index, it) -> select.apply(it))::iterator;
  }

  default <R, O extends One<R>> One<R> selectOne(final Func<? super T, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return new SelectQueryable<>(this, (index, it) -> selectOne.apply(it))::iterator;
  }

}

