package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    return One.done(cursor().select(select));
  }

  default <R, O extends One<R>> One<R> selectOne(final Func.Uni<? super T, ? extends O> select) {
    return One.done(cursor().selectNext(it -> select.tryApply(it).cursor()));
  }
}
