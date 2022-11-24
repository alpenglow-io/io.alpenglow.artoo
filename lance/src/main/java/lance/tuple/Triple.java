package lance.tuple;

import lance.func.Cons;
import lance.func.Func;

import static lance.tuple.Type.firstOf;
import static lance.tuple.Type.has;
import static lance.tuple.Type.secondOf;
import static lance.tuple.Type.thirdOf;

public interface Triple<A, B, C> extends Tuple {
  default A first() { return firstOf(this); }
  default B second() { return secondOf(this); }
  default C third() { return thirdOf(this); }

  default <T extends Record> T to(final Func.MaybeTriFunction<? super A, ? super B, ? super C, ? extends T> to) {
    return to.apply(first(), second(), third());
  }

  default <T> T as(final Func.MaybeTriFunction<? super A, ? super B, ? super C, ? extends T> as) {
    return as.apply(first(), second(), third());
  }

  default boolean is(final A value1, final B value2, final C value3) {
    return has(first(), value1) && has(second(), value2) && has(third(), value3);
  }

  default <R extends Record & Triple<A, B, C>> R map(Func.MaybeTriFunction<? super A, ? super B, ? super C, ? extends R> map) {
    return map.apply(first(), second(), third());
  }

  default <R extends Record & Triple<A, B, C>, F extends Record & Single<R>> R flatMap(Func.MaybeTriFunction<? super A, ? super B, ? super C, ? extends F> func) {
    return func.apply(first(), second(), third()).first();
  }

  default Triple<A, B, C> peek(Cons.MaybeTriConsumer<? super A, ? super B, ? super C> cons) {
    cons.accept(first(), second(), third());
    return this;
  }

}
