package lance.query.many.oftwo;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Pred;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.WhenType;
import lance.query.func.WhenWhere;
import lance.tuple.Pair;

@SuppressWarnings("unchecked")
public interface Matchable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> when(final Pred.TryBiPredicate<? super A, ? super B> pred, Cons.TryBiConsumer<? super A, ? super B> cons) {
    return () -> cursor().map(new WhenWhere<>(pair -> pred.tryTest(pair.first(), pair.second()), pair -> cons.tryAccept(pair.first(), pair.second())));
  }

  default <R> Many.OfTwo<A, B> when(final Class<R> type, Cons.TryConsumer<? super R> cons) {
    return when(type, unary(cons));
  }

  default <P> Many.OfTwo<A, B> when(final Class<P> type, Func.TryFunction<? super P, ? extends Pair<A, B>> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  private <R> Func.TryFunction<R, Pair<A, B>> unary(final Cons.TryConsumer<? super R> cons) {
    return it -> {
      cons.tryAccept(it);
      return (Pair<A, B>) it;
    };
  }
}
