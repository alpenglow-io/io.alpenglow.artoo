package io.artoo.lance.tuple;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.Type.firstOf;
import static io.artoo.lance.tuple.Type.has;

public interface Single<R extends Record & Single<R, A>, A> extends Tuple<R> {
  default A first() { return firstOf(this, type$()); }

  default <T extends Record> T to(final @NotNull Func.Uni<? super A, ? extends T> to) {
    return to.apply(first());
  }

  default <T> T as(final @NotNull Func.Uni<? super A, ? extends T> as) {
    return as.apply(first());
  }

  default boolean is(final A value) {
    return has(first(), value);
  }

  default R map(Func.Uni<? super A, ? extends R> map) {
    return map.apply(first());
  }

  default <F extends Record & Single<F, R>> R flatMap(Func.Uni<? super A, ? extends F> func) {
    return func.apply(first()).first();
  }

  @SuppressWarnings("unchecked")
  default R filter(Pred.Uni<? super A> pred) {
    return pred.apply(first()) ? (R) this : null;
  }

  @SuppressWarnings("unchecked")
  default R peek(Cons.Uni<? super A> cons) {
    cons.apply(first());
    return (R) this;
  }
}
