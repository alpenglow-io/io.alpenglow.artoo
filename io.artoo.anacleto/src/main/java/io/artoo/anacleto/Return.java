package io.artoo.anacleto;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Return<T, R> {
  static <T, R> Return<T, R> of(T any) {
    return then -> then.apply(any);
  }

  R then(Function<? super T, ? extends R> then);
  default R then(Supplier<? extends R> then) {
    return then(it -> then.get());
  }
}
