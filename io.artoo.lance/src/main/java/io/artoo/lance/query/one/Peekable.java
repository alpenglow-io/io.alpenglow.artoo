package io.artoo.lance.query.one;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Peek;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Peekable<T> extends Queryable<T> {
  default One<T> peek(final Cons.Uni<? super T> peek) {
    final var p = nonNullable(peek, "peek");
    return () -> cursor().map(new Peek<T, T>((i, it) -> p.tryAccept(it))).end();
  }
}
