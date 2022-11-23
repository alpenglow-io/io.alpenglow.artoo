package io.artoo.lance.query.one;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final Cons.MaybeConsumer<? super T> peek) {
    return () -> cursor().map(new Peek<T, T>((i, it) -> peek.tryAccept(it)));
  }

  default One<T> exceptionally(Cons.MaybeConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
