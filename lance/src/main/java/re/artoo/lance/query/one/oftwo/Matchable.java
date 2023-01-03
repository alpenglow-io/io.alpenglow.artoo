package re.artoo.lance.query.one.oftwo;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.WhenType;
import re.artoo.lance.query.closure.WhenWhere;
import re.artoo.lance.tuple.Pair;

@SuppressWarnings("unchecked")
public interface Matchable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> when(final TryPredicate2<? super A, ? super B> pred, TryConsumer2<? super A, ? super B> cons) {
    return () -> cursor().map(new WhenWhere<>(pair -> pred.invoke(pair.first(), pair.second()), pair -> cons.invoke(pair.first(), pair.second())));
  }

  default <R> One.OfTwo<A, B> when(final Class<R> type, TryConsumer1<? super R> cons) {
    return when(type, unary(cons));
  }

  default <P> One.OfTwo<A, B> when(final Class<P> type, TryFunction1<? super P, ? extends Pair<A, B>> func) {
    return () -> cursor().map(new WhenType<>(type, func));
  }

  private <R> TryFunction1<R, Pair<A, B>> unary(final TryConsumer1<? super R> cons) {
    return it -> {
      cons.invoke(it);
      return (Pair<A, B>) it;
    };
  }
}
