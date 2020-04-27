package artoo.query.one.internal;

import artoo.func.Func;
import artoo.query.internal.Select;
import artoo.query.One;
import artoo.query.internal.SelectQueryable;

import static artoo.type.Nullability.nonNullable;

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

