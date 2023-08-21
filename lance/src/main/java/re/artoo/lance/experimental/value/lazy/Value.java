package re.artoo.lance.experimental.value.lazy;

import re.artoo.lance.experimental.value.Lazy;
import re.artoo.lance.experimental.value.Lock;
import re.artoo.lance.func.TrySupplier1;

import static java.util.Objects.requireNonNull;

public final class Value<T> implements Lazy<T> {
  private static final Object NO_VALUE = new Object();
  private final Lock lock;
  private final TrySupplier1<? extends T> initialization;
  private volatile Object value;

  public Value(TrySupplier1<? extends T> initialization) {
    this(Lock.reentrant(), requireNonNull(initialization, "initialization can't be null"));
  }

  private Value(Lock lock, TrySupplier1<? extends T> initialization) {
    this.lock = lock;
    this.initialization = initialization;
    this.value = NO_VALUE;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T value() {
    return (T) lock.read(() -> value)
      .where(NO_VALUE::equals)
      .selection(it -> lock.write(() -> (value = initialization.invoke())))
      .coalesce(value)
      .yield();
  }
}
