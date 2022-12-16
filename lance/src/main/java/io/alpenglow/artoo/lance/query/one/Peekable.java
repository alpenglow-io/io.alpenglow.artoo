package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.closure.Peek;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final TryConsumer1<? super T> peek) {
    return () -> cursor().map(new Peek<T>((i, it) -> peek.invoke(it)));
  }

  default One<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
