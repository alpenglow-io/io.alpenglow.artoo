package io.artoo.lance.query.eventual;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Peek;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default Eventual<T> peek(final Cons.Uni<? super T> peek) {
    return () -> cursor().map(new Peek<>((i, it) -> peek.tryAccept(it)));
  }

  default Eventual<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}
