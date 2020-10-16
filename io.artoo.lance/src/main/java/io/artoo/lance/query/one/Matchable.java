package io.artoo.lance.query.one;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.WhenType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface Matchable<T> extends Queryable<T> {
  default <R> One<T> when(final Class<R> type, Cons.Uni<? super R> cons) {
    return () -> cursor().map(new WhenType<>(type, unary(cons)));
  }

  default <R> One<T> when(final Class<R> type, Func.Uni<? super R, ? extends T> func) {
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
