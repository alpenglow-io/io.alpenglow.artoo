package dev.lug.oak.func.fun;

@FunctionalInterface
public interface IntFunction2<T, R> extends Function2<Integer, T, R> {
  R applyInt(final int param1, T param2);

  @Override
  default R apply(Integer param1, T param2) {
    return applyInt(param1, param2);
  }
}
