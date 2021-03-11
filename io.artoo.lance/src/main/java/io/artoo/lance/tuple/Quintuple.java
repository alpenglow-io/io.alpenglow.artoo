package io.artoo.lance.tuple;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.tuple.Type.componentOf;
import static io.artoo.lance.tuple.Type.fifthOf;
import static io.artoo.lance.tuple.Type.firstOf;
import static io.artoo.lance.tuple.Type.forthOf;
import static io.artoo.lance.tuple.Type.has;
import static io.artoo.lance.tuple.Type.secondOf;
import static io.artoo.lance.tuple.Type.thirdOf;

public interface Quintuple<R extends Record & Quintuple<R, A, B, C, D, E>, A, B, C, D, E> extends Tuple<R> {
  default A first() { return firstOf(this, type$()); }
  default B second() { return secondOf(this, type$()); }
  default C third() { return thirdOf(this, type$()); }
  default D forth() { return forthOf(this, type$()); }
  default E fifth() { return fifthOf(this, type$()); }

  default <N extends Record> N to(final @NotNull Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends N> to) {
    return to.apply(first(), second(), third(), forth());
  }

  default <T extends Record> T as(final @NotNull Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> as) {
    return as.apply(first(), second(), third(), forth(), fifth());
  }

  default boolean is(final A value1, final B value2, final C value3, final D value4, final E value5) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3) && has(forth(), value4) && has(fifth(), value5);
  }

  default R map(Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> map) {
    return map.apply(first(), second(), third(), forth(), fifth());
  }

  default <F extends Record & Single<F, R>> R flatMap(Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> func) {
    return func.apply(first(), second(), third(), forth(), fifth()).first();
  }

  @SuppressWarnings("unchecked")
  default R filter(Pred.Quin<? super A, ? super B, ? super C, ? super D, ? super E> pred) {
    return pred.apply(first(), second(), third(), forth(), fifth()) ? (R) this : null;
  }

  @SuppressWarnings("unchecked")
  default R peek(Cons.Quin<? super A, ? super B, ? super C, ? super D, ? super E> cons) {
    cons.apply(first(), second(), third(), forth(), fifth());
    return (R) this;
  }
}
