package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiConsumer;
import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default Many<T> peek(TryConsumer<? super T> peek) {
    return peek((index, it) -> peek.tryAccept(it));
  }

  default Many<T> peek(TryBiConsumer<? super Integer, ? super T> peek) {
    return () -> cursor().map(new Peek<T, T>(peek));
  }

  default Many<T> exceptionally(TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

