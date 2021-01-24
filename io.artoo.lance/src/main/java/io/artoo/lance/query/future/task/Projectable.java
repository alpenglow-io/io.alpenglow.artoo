package io.artoo.lance.query.future.task;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.future.Futureable;
import io.artoo.lance.query.future.Task;
import io.artoo.lance.query.oper.Select;

@FunctionalInterface
public interface Projectable<T> extends Futureable<T> {
  default <R> Task<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> future().map(new Select<>(select));
  }

  default <R, O extends Task<R>> Task<R> selection(final Func.Uni<? super T, ? extends O> select) {
    return () -> future().map(new Select<>(select)).flatMap(Futureable::future);
  }
}
