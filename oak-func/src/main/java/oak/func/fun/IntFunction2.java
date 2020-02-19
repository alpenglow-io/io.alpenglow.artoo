package oak.func.fun;

import oak.func.$2.Func;

@FunctionalInterface
public interface IntFunction2<T, R> extends Func<Integer, T, R> {
  R applyInt(final int param1, T param2);

  @Override
  default R apply(Integer param1, T param2) {
    return applyInt(param1, param2);
  }
}
