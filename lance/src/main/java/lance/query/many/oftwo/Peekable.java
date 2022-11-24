package lance.query.many.oftwo;

import lance.func.Cons;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.Peek;
import lance.tuple.Pair;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> peek(Cons.MaybeBiConsumer<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.tryAccept(first, second));
  }

  default Many.OfTwo<A, B> peek(Cons.MaybeTriConsumer<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<Pair<A, B>, Pair<A, B>>((index, record) -> peek.tryAccept(index, record.first(), record.second())));
  }

  default Many.OfTwo<A, B> exceptionally(Cons.MaybeConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

