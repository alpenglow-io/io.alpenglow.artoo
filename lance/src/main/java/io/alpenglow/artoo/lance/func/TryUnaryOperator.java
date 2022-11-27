package io.alpenglow.artoo.lance.func;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface TryUnaryOperator<T> extends UnaryOperator<T> {
  T tryApply(T t) throws Throwable;
  @Override
  default T apply(T t) {
    try {
      return tryApply(t);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
