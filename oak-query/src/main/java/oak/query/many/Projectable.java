package oak.query.many;

import oak.func.$2.FuncInt;
import oak.func.Func;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.internal.Select;
import oak.query.internal.SelectQueryable;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(FuncInt<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, select)::iterator;
  }

  default <R> Many<R> select(Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (index, value) -> select.apply(value))::iterator;
  }

  default <R, M extends Many<R>> Many<R> selectMany(FuncInt<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectQueryable<>(this, select)::iterator;
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectQueryable<>(this, (index, it) -> select.apply(it))::iterator;
  }
}

