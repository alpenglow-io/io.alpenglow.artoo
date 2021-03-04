package io.artoo.lance.tuple;

import io.artoo.lance.func.Func;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.TupleType.has;
import static io.artoo.lance.tuple.TupleType.tryComponentOf;

public interface Pair<R extends Record, A, B> extends Single<R, A> {
  default B second() { return tryComponentOf(this, type$(), 1); }

  default <T extends Record> T to(final @NotNull Func.Bi<? super A, ? super B, ? extends T> to) {
    return to.apply(first(), second());
  }

  default <T> T as(final @NotNull Func.Bi<? super A, ? super B, ? extends T> as) {
    return as.apply(first(), second());
  }

  default boolean is(final A value1, final B value2) {
    return is(value1) && has(second(), value2);
  }
}
