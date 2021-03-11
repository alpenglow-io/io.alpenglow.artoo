package io.artoo.lance.tuple;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.Type.firstOf;
import static io.artoo.lance.tuple.Type.has;
import static io.artoo.lance.tuple.Type.secondOf;


public interface Pair<R extends Record & Pair<R, A, B>, A, B> extends Tuple<R> {
  default A first() { return firstOf(this, type$()); }
  default B second() { return secondOf(this, type$()); }

  default <T extends Record> T to(final @NotNull Func.Bi<? super A, ? super B, ? extends T> to) {
    return to.apply(first(), second());
  }

  default <T> T as(final @NotNull Func.Bi<? super A, ? super B, ? extends T> as) {
    return as.apply(first(), second());
  }

  default boolean is(final A value1, final B value2) {
    return has(first(), value1) && has(second(), value2);
  }

  default R map(Func.Bi<? super A, ? super B, ? extends R> map) {
    return map.apply(first(), second());
  }

  default <F extends Record & Single<F, R>> R flatMap(Func.Bi<? super A, ? super B, ? extends F> func) {
    return func.apply(first(), second()).first();
  }

  @SuppressWarnings("unchecked")
  default R filter(Pred.Bi<? super A, ? super B> pred) {
    return pred.apply(first(), second()) ? (R) this : null;
  }

  @SuppressWarnings("unchecked")
  default R peek(Cons.Bi<? super A, ? super B> cons) {
    cons.apply(first(), second());
    return (R) this;
  }
}
