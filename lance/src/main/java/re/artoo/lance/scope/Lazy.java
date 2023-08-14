package re.artoo.lance.scope;

import re.artoo.lance.func.InvokeException;
import re.artoo.lance.func.TrySupplier1;

import static java.util.Objects.requireNonNull;

public sealed interface Lazy<T> {
  static <T> Lazy<T> value(TrySupplier1<? extends T> initialization) {
    return new Later<>(Lock.reentrant(), requireNonNull(initialization, "initialization can't be null"));
  }

  T value();

  final class Later<T> implements Lazy<T> {
    private static final Object NOTHING = new Object();
    private final Lock lock;
    private final TrySupplier1<? extends T> initialization;
    private volatile Object value;

    Later(Lock lock, TrySupplier1<? extends T> initialization) {
      this.lock = lock;
      this.initialization = initialization;
      this.value = NOTHING;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T value() {
      return (T) lock.read(() -> value)
        .where(it -> it.equals(NOTHING))
        .selection(it -> lock.write(() -> (value = initialization.get())))
        .otherwise(value);
    }
  }
}
