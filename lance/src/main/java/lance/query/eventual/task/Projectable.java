package lance.query.eventual.task;

import lance.func.Func;
import lance.query.One;
import lance.query.eventual.Eventual;
import lance.query.eventual.Task;

public interface Projectable<T> extends Eventual<T> {
  default <R> Task<R> select(final Func.MaybeFunction<? super T, ? extends R> select) {
    return null;
  }

  default <R, O extends One<R>> Task<R> selection(final Func.MaybeFunction<? super T, ? extends O> selection) {
    return null;
  }
}
