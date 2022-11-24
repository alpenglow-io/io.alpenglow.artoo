package lance.query.many.oftwo;

import lance.func.Pred;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.Skip;
import lance.query.func.Take;
import lance.tuple.Pair;

public interface Partitionable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> skip(final int until) {
    return skipWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> skipWhile(final Pred.TryBiPredicate<? super A, ? super B> where) {
    return skipWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> skipWhile(final Pred.TryTriPredicate<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Skip<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }

  default Many.OfTwo<A, B> take(final int until) {
    return takeWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> takeWhile(final Pred.TryBiPredicate<? super A, ? super B> where) {
    return takeWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> takeWhile(final Pred.TryTriPredicate<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Take<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }
}
