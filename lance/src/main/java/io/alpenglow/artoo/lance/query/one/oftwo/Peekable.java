package io.alpenglow.artoo.lance.query.one.oftwo;

import io.alpenglow.artoo.lance.func.TryBiConsumer;
import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.func.TryTriConsumer;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Peek;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> peek(TryBiConsumer<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.tryAccept(first, second));
  }

  default One.OfTwo<A, B> peek(TryTriConsumer<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<Pair<A, B>, Pair<A, B>>((index, record) -> peek.tryAccept(index, record.first(), record.second())));
  }

  default One.OfTwo<A, B> exceptionally(TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

