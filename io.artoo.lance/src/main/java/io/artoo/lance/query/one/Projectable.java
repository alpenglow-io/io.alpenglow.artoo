package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(new Select<>((i, it) -> select.tryApply(it)));
  }

  default <R, O extends One<R>> One<R> selection(final Func.Uni<? super T, ? extends O> selectOne) {
    return () -> cursor().map(new Select<>((i, it) -> selectOne.tryApply(it))).flatMap(Queryable::cursor);
  }
}
