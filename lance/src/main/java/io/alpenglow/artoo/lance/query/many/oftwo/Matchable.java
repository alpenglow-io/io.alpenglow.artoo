package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryConsumer2;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.WhenType;
import io.alpenglow.artoo.lance.query.func.WhenWhere;
import io.alpenglow.artoo.lance.tuple.Pair;

@SuppressWarnings("unchecked")
public interface Matchable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> when(final TryPredicate2<? super A, ? super B> pred, TryConsumer2<? super A, ? super B> cons) {
    return () -> cursor().map(new WhenWhere<>(pair -> pred.tryTest(pair.first(), pair.second()), pair -> cons.tryAccept(pair.first(), pair.second())));
  }

  default <R> Many.OfTwo<A, B> when(final Class<R> type, TryConsumer1<? super R> cons) {
    return when(type, unary(cons));
  }

  default <P> Many.OfTwo<A, B> when(final Class<P> type, TryFunction1<? super P, ? extends Pair<A, B>> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  private <R> TryFunction1<R, Pair<A, B>> unary(final TryConsumer1<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (Pair<A, B>) it;
    };
  }
}