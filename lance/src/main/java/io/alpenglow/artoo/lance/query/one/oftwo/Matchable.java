package io.alpenglow.artoo.lance.query.one.oftwo;

import io.alpenglow.artoo.lance.func.TryBiConsumer;
import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.WhenType;
import io.alpenglow.artoo.lance.query.func.WhenWhere;
import io.alpenglow.artoo.lance.tuple.Pair;

@SuppressWarnings("unchecked")
public interface Matchable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> when(final TryBiPredicate<? super A, ? super B> pred, TryBiConsumer<? super A, ? super B> cons) {
    return () -> cursor().map(new WhenWhere<>(pair -> pred.tryTest(pair.first(), pair.second()), pair -> cons.tryAccept(pair.first(), pair.second())));
  }

  default <R> One.OfTwo<A, B> when(final Class<R> type, TryConsumer<? super R> cons) {
    return when(type, unary(cons));
  }

  default <P> One.OfTwo<A, B> when(final Class<P> type, TryFunction<? super P, ? extends Pair<A, B>> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  private <R> TryFunction<R, Pair<A, B>> unary(final TryConsumer<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (Pair<A, B>) it;
    };
  }
}
