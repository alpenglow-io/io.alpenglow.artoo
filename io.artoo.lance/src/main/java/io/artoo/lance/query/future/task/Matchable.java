package io.artoo.lance.query.future.task;

import io.artoo.lance.NotImplementedYetException;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.future.Futureable;
import io.artoo.lance.query.future.Task;
import io.artoo.lance.query.oper.WhenType;
import io.artoo.lance.query.oper.WhenWhere;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Matchable<T> extends Futureable<T> {
  default Task<T> when(final Pred.Uni<? super T> pred, Cons.Uni<? super T> cons) {
    throw new NotImplementedYetException("When should be defined!");
  }

  default <R> Task<T> when(final Class<R> type, Cons.Uni<? super R> cons) {
    throw new NotImplementedYetException("When should be defined!");
  }

  default <R> Task<T> when(final Class<R> type, Func.Uni<? super R, ? extends T> func) {
    throw new NotImplementedYetException("When should be defined!");
  }

  @NotNull
  private <R> Func.Uni<R, T> unary(final Cons.Uni<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (T) it;
    };
  }
}
