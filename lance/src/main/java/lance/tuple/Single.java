package lance.tuple;

import lance.func.Cons.TryConsumer;
import lance.func.Func.TryFunction;

import static lance.tuple.Type.firstOf;
import static lance.tuple.Type.has;

public interface Single<A> extends Tuple {
  default A first() { return firstOf(this); }

  default <T extends Record> T to(final TryFunction<? super A, ? extends T> to) {
    return to.apply(first());
  }

  default <T> T as(final TryFunction<? super A, ? extends T> as) {
    return as.apply(first());
  }

  default boolean is(final A value) {
    return has(first(), value);
  }

  default <R extends Record & Single<A>> R map(TryFunction<? super A, ? extends R> map) {
    return map.apply(first());
  }

  default <R extends Record & Single<A>, F extends Record & Single<R>> R flatMap(TryFunction<? super A, ? extends F> func) {
    return func.apply(first()).first();
  }

  default Single<A> peek(TryConsumer<? super A> cons) {
    cons.accept(first());
    return this;
  }
}
