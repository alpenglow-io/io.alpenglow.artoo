package trydent.query.one.internal;

import trydent.func.Func;
import trydent.query.internal.Select;
import trydent.query.One;
import trydent.query.internal.SelectQueryable;

import static trydent.type.Nullability.nonNullable;

public interface Projectable<T> extends Sneakable<T> {
  default <R> One<R> select(final Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, this.sneak(), (index, it) -> select.apply(it))::iterator;
  }

  default <R, O extends One<R>> One<R> selectOne(final Func<? super T, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return new SelectQueryable<>(this, this.sneak(), (index, it) -> selectOne.apply(it))::iterator;
  }

}

