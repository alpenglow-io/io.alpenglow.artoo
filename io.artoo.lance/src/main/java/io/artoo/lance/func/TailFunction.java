package io.artoo.lance.func;

import io.artoo.lance.func.Func.MaybeFunction;
import io.artoo.lance.func.TailFunction.Return;
import io.artoo.lance.scope.Maybe;

import java.util.concurrent.atomic.AtomicReference;

public interface TailFunction<T, R, F extends TailFunction<T, R, F>> extends MaybeFunction<T, Return<T, R, F>> {


  record Return<T, R, F extends MaybeFunction<T, Return<T, R, F>>>(R result, F next) {
    public static <T, R, F extends MaybeFunction<T, Return<T, R, F>>> Return<T, R, F> with(R result, F nextApply) {
      return new Return<>(result, nextApply);
    }
  }

  abstract class Tailrec<T, M, F extends Tailrec<T, M, F> & TailFunction<T, M, F>> implements TailFunction<T, M, F> {
    private final AtomicReference<F> reference = new AtomicReference<>();

    @SuppressWarnings("unchecked")
    private <R> Maybe<R> let(final Func.MaybeFunction<? super F, ? extends R> map, final Func.MaybeFunction<? super R, ? extends F> update) {
      reference.compareAndSet(null, (F) this);
      F prev = reference.get() , next = null;
      R applied = null;
      for (var haveNext = false;;) {
        if (!haveNext) {
          applied = map.apply(prev);
          next = update.apply(applied);
        }
        if (reference.weakCompareAndSetVolatile(prev, next))
          return Maybe.value(applied);
        haveNext = (prev == (prev = reference.get()));
      }
    }

    public Return<T, M, F> on(T element) {
      return let(it -> it.apply(element), TailFunction.Return::next).otherwise("Can't reference function", IllegalStateException::new);
    }
  }
}
