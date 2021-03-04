package io.artoo.lance.tuple;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.TupleType.has;
import static io.artoo.lance.tuple.TupleType.tryComponentOf;

public interface Single<R extends Record, A> extends One<A>, Tuple<R> {
  default A first() { return tryComponentOf(this, type$(), 0); }

  default <T extends Record> T to(final @NotNull Func.Uni<? super A, ? extends T> to) {
    return to.apply(first());
  }

  default <T> T as(final @NotNull Func.Uni<? super A, ? extends T> as) {
    return as.apply(first());
  }

  default boolean is(final A value) {
    return has(first(), value);
  }

  @Override
  default Cursor<A> cursor() {
    return Cursor.open(first());
  }
}
