package io.artoo.lance.query.many;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.impl.WhenType;
import io.artoo.lance.query.impl.WhenWhere;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface Matchable<T> extends Queryable<T> {
  default Many<T> when(final Pred.Uni<? super T> pred, Cons.Uni<? super T> cons) {
    return () -> cursor().map(new WhenWhere<>(pred, cons));
  }

  default <R> Many<T> when(final Class<R> type, Cons.Uni<? super R> cons) {
    return when(type, unary(cons));
  }

  default <R> Many<T> when(final Class<R> type, Func.Uni<? super R, ? extends T> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  @NotNull
  private <R> Func.Uni<R, T> unary(final Cons.Uni<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (T) it;
    };
  }
}
