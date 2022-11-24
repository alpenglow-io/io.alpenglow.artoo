package lance.tuple;

import lance.func.Cons.MaybeConsumer;
import lance.func.Func.MaybeFunction;

import static lance.tuple.Type.firstOf;
import static lance.tuple.Type.has;

public interface Single<A> extends Tuple {
  default A first() { return firstOf(this); }

  default <T extends Record> T to(final MaybeFunction<? super A, ? extends T> to) {
    return to.apply(first());
  }

  default <T> T as(final MaybeFunction<? super A, ? extends T> as) {
    return as.apply(first());
  }

  default boolean is(final A value) {
    return has(first(), value);
  }

  default <R extends Record & Single<A>> R map(MaybeFunction<? super A, ? extends R> map) {
    return map.apply(first());
  }

  default <R extends Record & Single<A>, F extends Record & Single<R>> R flatMap(MaybeFunction<? super A, ? extends F> func) {
    return func.apply(first()).first();
  }

  default Single<A> peek(MaybeConsumer<? super A> cons) {
    cons.accept(first());
    return this;
  }
}
