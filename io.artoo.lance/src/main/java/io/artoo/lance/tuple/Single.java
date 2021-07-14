package io.artoo.lance.tuple;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.Type.firstOf;
import static io.artoo.lance.tuple.Type.has;

public interface Single<A> extends Tuple {
  default A first() { return firstOf(this); }

  default <T extends Record> T to(final @NotNull Func.Uni<? super A, ? extends T> to) {
    return to.apply(first());
  }

  default <T> T as(final @NotNull Func.Uni<? super A, ? extends T> as) {
    return as.apply(first());
  }

  default boolean is(final A value) {
    return has(first(), value);
  }

  default <R extends Record & Single<A>> R map(Func.Uni<? super A, ? extends R> map) {
    return map.apply(first());
  }

  default <R extends Record & Single<A>, F extends Record & Single<R>> R flatMap(Func.Uni<? super A, ? extends F> func) {
    return func.apply(first()).first();
  }

  default Single<A> peek(Cons.Uni<? super A> cons) {
    cons.apply(first());
    return this;
  }
}
