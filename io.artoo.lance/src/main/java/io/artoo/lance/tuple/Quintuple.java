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

public interface Quintuple<A, B, C, D, E> extends Tuple {
  default A first() { return firstOf(this); }
  default B second() { return secondOf(this); }
  default C third() { return thirdOf(this); }
  default D forth() { return forthOf(this); }
  default E fifth() { return fifthOf(this); }

  default <N extends Record> N to(final @NotNull Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends N> to) {
    return to.apply(first(), second(), third(), forth(), fifth());
  }

  default <T extends Record> T as(final @NotNull Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> as) {
    return as.apply(first(), second(), third(), forth(), fifth());
  }

  default boolean is(final A value1, final B value2, final C value3, final D value4, final E value5) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3) && has(forth(), value4) && has(fifth(), value5);
  }

  default <R extends Record & Quintuple<A, B, C, D, E>> R map(Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> map) {
    return map.apply(first(), second(), third(), forth(), fifth());
  }

  default <R extends Record & Quintuple<A, B, C, D, E>, F extends Record & Single<R>> R flatMap(Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> func) {
    return func.apply(first(), second(), third(), forth(), fifth()).first();
  }

  default Quintuple<A, B, C, D, E> peek(Cons.Quin<? super A, ? super B, ? super C, ? super D, ? super E> cons) {
    cons.apply(first(), second(), third(), forth(), fifth());
    return this;
  }
}
