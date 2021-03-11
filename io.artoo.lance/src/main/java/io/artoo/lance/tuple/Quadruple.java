package io.artoo.lance.tuple;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.Type.firstOf;
import static io.artoo.lance.tuple.Type.forthOf;
import static io.artoo.lance.tuple.Type.has;
import static io.artoo.lance.tuple.Type.secondOf;
import static io.artoo.lance.tuple.Type.thirdOf;

public interface Quadruple<R extends Record & Quadruple<R, A, B, C, D>, A, B, C, D> extends Tuple<R> {
  default A first() { return firstOf(this, type$()); }
  default B second() { return secondOf(this, type$()); }
  default C third() { return thirdOf(this, type$()); }
  default D forth() { return forthOf(this, type$()); }

  default <N extends Record> N to(final @NotNull Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends N> to) {
    return to.apply(first(), second(), third(), forth());
  }

  default <T> T as(final @NotNull Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends T> as) {
    return as.apply(first(), second(), third(), forth());
  }

  default boolean is(final A value1, final B value2, final C value3, final D value4) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3) && has(forth(), value4);
  }

  default R map(Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends R> map) {
    return map.apply(first(), second(), third(), forth());
  }

  default <F extends Record & Single<F, R>> R flatMap(Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends F> func) {
    return func.apply(first(), second(), third(), forth()).first();
  }

  @SuppressWarnings("unchecked")
  default R filter(Pred.Quad<? super A, ? super B, ? super C, ? super D> pred) {
    return pred.apply(first(), second(), third(), forth()) ? (R) this : null;
  }

  @SuppressWarnings("unchecked")
  default R peek(Cons.Quad<? super A, ? super B, ? super C, ? super D> cons) {
    cons.apply(first(), second(), third(), forth());
    return (R) this;
  }
}
