package oak.query.one.internal;

import oak.query.internal.Project;
import oak.query.internal.Projection;
import oak.query.One;

import static oak.type.Nullability.nonNullable;

public interface Projectable<T> extends Sneakable<T> {
  default <R> One<R> select(final oak.query.one.Projectable.Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Project<T, R>(this, this.sneak(), (index, it) -> select.apply(it))::iterator;
  }

  default <R, O extends One<R>> One<R> select(final oak.query.one.Projectable.SelectOne<? super T, ? extends R, ? extends O> selectOne) {
    nonNullable(selectOne, "selectOne");
    return new Projection<>(this, this.sneak(), (index, it) -> selectOne.apply(it))::iterator;
  }

}

