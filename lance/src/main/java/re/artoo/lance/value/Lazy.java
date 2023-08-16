package re.artoo.lance.value;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.lazy.Value;

import static java.util.Objects.requireNonNull;

public sealed interface Lazy<T> permits Value {
  static <T> Lazy<T> value(TrySupplier1<? extends T> initialization) {
    return lazy(initialization);
  }

  static <T> Lazy<T> lazy(TrySupplier1<? extends T> initialization) {
    return new Value<>(initialization);
  }

  T value();
}
