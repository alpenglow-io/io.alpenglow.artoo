package re.artoo.lance.value.lazy;

import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.Lazy;
import re.artoo.lance.value.Lock;

import static java.util.Objects.requireNonNull;

public final class Value<T> implements Lazy<T> {
  private static final Object NOTHING = new Object();
  private final Lock lock;
  private final TrySupplier1<? extends T> initialization;
  private volatile Object value;

  public Value(TrySupplier1<? extends T> initialization) {
    this(Lock.reentrant(), requireNonNull(initialization, "initialization can't be null"));
  }

  private Value(Lock lock, TrySupplier1<? extends T> initialization) {
    this.lock = lock;
    this.initialization = initialization;
    this.value = NOTHING;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T value() {
    return (T) lock.read(() -> value)
      .where(it -> it.equals(NOTHING))
      .selectOne(it -> lock.write(() -> (value = initialization.invoke())))
      .otherwise(value);
  }
}
