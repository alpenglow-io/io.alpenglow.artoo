package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryConsumer2;
import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.closure.Peek;

public interface Peekable<T> extends Queryable<T> {
  default Many<T> peek(TryConsumer1<? super T> peek) {
    return peek((index, it) -> peek.invoke(it));
  }

  default Many<T> peek(TryConsumer2<? super Integer, ? super T> peek) {
    return () -> cursor().map(new Peek<T, T>(peek));
  }

  default Many<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

