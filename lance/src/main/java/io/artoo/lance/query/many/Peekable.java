package io.artoo.lance.query.many;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Peek;

public interface Peekable<T> extends Queryable<T> {
  default Many<T> peek(Cons.MaybeConsumer<? super T> peek) {
    return peek((index, it) -> peek.tryAccept(it));
  }

  default Many<T> peek(Cons.MaybeBiConsumer<? super Integer, ? super T> peek) {
    return () -> cursor().map(new Peek<T, T>(peek));
  }

  default Many<T> exceptionally(Cons.MaybeConsumer<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

