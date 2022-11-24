package lance.func;

import lance.func.Func.TryFunction;
import lance.func.Recursive.Return;
import lance.scope.Maybe;

import java.util.concurrent.atomic.AtomicReference;

public interface Recursive<PARAMETER, RESULT, FUNCTION extends Recursive<PARAMETER, RESULT, FUNCTION>> extends TryFunction<PARAMETER, Return<PARAMETER, RESULT, FUNCTION>> {


  record Return<T, R, F extends TryFunction<T, Return<T, R, F>>>(R result, F next) {
    public static <T, R, F extends TryFunction<T, Return<T, R, F>>> Return<T, R, F> with(R result, F nextApply) {
      return new Return<>(result, nextApply);
    }
  }

  abstract class Tailrec<PARAMETER, RETURN, FUNCTION extends Tailrec<PARAMETER, RETURN, FUNCTION> & Recursive<PARAMETER, RETURN, FUNCTION>> implements Recursive<PARAMETER, RETURN, FUNCTION> {
    private final AtomicReference<FUNCTION> reference = new AtomicReference<>();

    @SuppressWarnings("unchecked")
    private <RESULT> Maybe<RESULT> let(final TryFunction<? super FUNCTION, ? extends RESULT> current, final TryFunction<? super RESULT, ? extends FUNCTION> update) {
      reference.compareAndSet(null, (FUNCTION) this);
      FUNCTION prev = reference.get() , next = null;
      RESULT result = null;
      for (var haveNext = false;;) {
        if (!haveNext) {
          result = current.apply(prev);
          next = update.apply(result);
        }
        if (reference.weakCompareAndSetVolatile(prev, next))
          return Maybe.value(result);
        haveNext = (prev == (prev = reference.get()));
      }
    }

    public Return<PARAMETER, RETURN, FUNCTION> on(PARAMETER parameter) {
      return let(it -> it.apply(parameter), Recursive.Return::next).otherwise("Can't reference function", IllegalStateException::new);
    }
  }
}
