package io.artoo.lance.query.one.oftwo;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.WhenType;
import io.artoo.lance.query.internal.WhenWhere;
import io.artoo.lance.tuple.Pair;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface Matchable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> when(final Pred.Bi<? super A, ? super B> pred, Cons.Bi<? super A, ? super B> cons) {
    return () -> cursor().map(new WhenWhere<>(pair -> pred.tryTest(pair.first(), pair.second()), pair -> cons.tryAccept(pair.first(), pair.second())));
  }

  default <R> One.OfTwo<A, B> when(final Class<R> type, Cons.Uni<? super R> cons) {
    return when(type, unary(cons));
  }

  default <P> One.OfTwo<A, B> when(final Class<P> type, Func.Uni<? super P, ? extends Pair<A, B>> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  @NotNull
  private <R> Func.Uni<R, Pair<A, B>> unary(final Cons.Uni<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (Pair<A, B>) it;
    };
  }
}
