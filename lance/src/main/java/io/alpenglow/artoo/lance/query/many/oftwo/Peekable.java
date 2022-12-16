package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryConsumer2;
import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryConsumer3;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.closure.Peek;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> peek(TryConsumer2<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.invoke(first, second));
  }

  default Many.OfTwo<A, B> peek(TryConsumer3<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<>((index, record) -> peek.invoke(index, record.first(), record.second())));
  }

  default Many.OfTwo<A, B> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

