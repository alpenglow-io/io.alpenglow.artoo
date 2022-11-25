package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.func.TryQuinConsumer;
import io.alpenglow.artoo.lance.func.TryQuinFunction;

import static io.alpenglow.artoo.lance.tuple.Type.fifthOf;
import static io.alpenglow.artoo.lance.tuple.Type.firstOf;
import static io.alpenglow.artoo.lance.tuple.Type.forthOf;
import static io.alpenglow.artoo.lance.tuple.Type.has;
import static io.alpenglow.artoo.lance.tuple.Type.secondOf;
import static io.alpenglow.artoo.lance.tuple.Type.thirdOf;

public interface Quintuple<A, B, C, D, E> extends Tuple {
  default A first() { return firstOf(this); }
  default B second() { return secondOf(this); }
  default C third() { return thirdOf(this); }
  default D forth() { return forthOf(this); }
  default E fifth() { return fifthOf(this); }

  default <N extends Record> N to(final TryQuinFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends N> to) {
    return to.apply(first(), second(), third(), forth(), fifth()).nullable();
  }

  default <T extends Record> T as(final TryQuinFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> as) {
    return as.apply(first(), second(), third(), forth(), fifth()).nullable();
  }

  default boolean is(final A value1, final B value2, final C value3, final D value4, final E value5) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3) && has(forth(), value4) && has(fifth(), value5);
  }

  default <R extends Record & Quintuple<A, B, C, D, E>> R map(TryQuinFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> map) {
    return map.apply(first(), second(), third(), forth(), fifth()).nullable();
  }

  default <R extends Record & Quintuple<A, B, C, D, E>, F extends Record & Single<R>> R flatMap(TryQuinFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends F> func) {
    return func.apply(first(), second(), third(), forth(), fifth()).nullable().first();
  }

  default Quintuple<A, B, C, D, E> peek(TryQuinConsumer<? super A, ? super B, ? super C, ? super D, ? super E> cons) {
    cons.accept(first(), second(), third(), forth(), fifth());
    return this;
  }
}
