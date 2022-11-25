package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryConsumer;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final TryConsumer<? super T> peek) {
    return () -> cursor().map(new Peek<T, T>((i, it) -> peek.tryAccept(it)));
  }

  default One<T> exceptionally(TryConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
