package artoo.query.one.impl;

import artoo.func.Func;
import artoo.query.One;
import artoo.query.impl.Select;
import artoo.query.impl.SelectMany;

import static artoo.type.Nullability.nonNullable;

public interface Projectable<T> extends Sneakable<T> {
  default <R> One<R> select(final Func<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, this.sneak(), (index, it) -> select.apply(it))::iterator;
  }

  default <R, O extends One<R>> One<R> selectOne(final Func<? super T, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return new SelectMany<>(this, this.sneak(), (index, it) -> selectOne.apply(it))::iterator;
  }

}

