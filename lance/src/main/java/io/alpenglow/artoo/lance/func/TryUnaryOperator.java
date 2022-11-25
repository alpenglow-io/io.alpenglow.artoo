package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface TryUnaryOperator<T> extends UnaryOperator<T> {
  T tryApply(T t) throws Throwable;

  default Maybe<T> maybeApply(T t) {
    try {
      return Maybe.value(tryApply(t));
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }

  @Override
  default T apply(T t) {
    return maybeApply(t).otherwise("Can't result unary operator", IllegalCallerException::new);
  }
}
