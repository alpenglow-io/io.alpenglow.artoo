package artoo.query.many;

import artoo.func.$2.FuncInt;
import artoo.func.Func;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.impl.Select;
import artoo.query.impl.SelectMany;

import static artoo.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> selectIth(FuncInt<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, select)::iterator;
  }

  default <R> Many<R> select(Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, (index, value) -> select.apply(value))::iterator;
  }

  default <R, M extends Many<R>> Many<R> selectManyIth(FuncInt<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, select)::iterator;
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (index, it) -> select.apply(it))::iterator;
  }
}

