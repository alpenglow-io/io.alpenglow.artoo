package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Peek;
import io.artoo.lance.tuple.Pair;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> peek(Cons.Bi<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.tryAccept(first, second));
  }

  default Many.OfTwo<A, B> peek(Cons.Tri<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<Pair<A, B>, Pair<A, B>>((index, record) -> peek.tryAccept(index, record.first(), record.second())));
  }

  default Many.OfTwo<A, B> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

