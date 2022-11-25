package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.func.TryQuadConsumer;
import io.alpenglow.artoo.lance.func.TryQuadFunction;

import static io.alpenglow.artoo.lance.tuple.Type.firstOf;
import static io.alpenglow.artoo.lance.tuple.Type.forthOf;
import static io.alpenglow.artoo.lance.tuple.Type.has;
import static io.alpenglow.artoo.lance.tuple.Type.secondOf;
import static io.alpenglow.artoo.lance.tuple.Type.thirdOf;

public interface Quadruple<A, B, C, D> extends Tuple {
  default A first() { return firstOf(this); }
  default B second() { return secondOf(this); }
  default C third() { return thirdOf(this); }
  default D forth() { return forthOf(this); }

  default <N extends Record> N to(final TryQuadFunction<? super A, ? super B, ? super C, ? super D, ? extends N> to) {
    return to.apply(first(), second(), third(), forth());
  }

  default <T> T as(final TryQuadFunction<? super A, ? super B, ? super C, ? super D, ? extends T> as) {
    return as.apply(first(), second(), third(), forth());
  }

  default boolean is(final A value1, final B value2, final C value3, final D value4) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3) && has(forth(), value4);
  }

  default <R extends Record & Quadruple<A, B, C, D>> R map(TryQuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> map) {
    return map.apply(first(), second(), third(), forth());
  }

  default <R extends Record & Quadruple<A, B, C, D>, F extends Record & Single<R>> R flatMap(TryQuadFunction<? super A, ? super B, ? super C, ? super D, ? extends F> func) {
    return func.apply(first(), second(), third(), forth()).first();
  }

  default Quadruple<A, B, C, D> peek(TryQuadConsumer<? super A, ? super B, ? super C, ? super D> cons) {
    cons.accept(first(), second(), third(), forth());
    return this;
  }
}
