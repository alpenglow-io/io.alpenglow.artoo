package trydent.query.many;

import trydent.func.$2.FuncInt;
import trydent.func.Func;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.internal.Select;
import trydent.query.internal.SelectQueryable;

import static trydent.type.Nullability.nonNullable;

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

