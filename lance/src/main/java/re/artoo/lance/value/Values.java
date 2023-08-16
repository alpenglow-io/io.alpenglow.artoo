package re.artoo.lance.value;

import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public sealed interface Values<T> permits ReentrantLocked {
  @SafeVarargs
  static <T> Values<T> lock(T... values) {
    return new ReentrantLocked<>(values);
  }

  static <T> Values<T> empty() {
    return lock();
  }

  Values<T> push(T value);

  T pop();
}

final class ReentrantLocked<T> implements Values<T> {
  private final ReadWriteLock reentrant = new ReentrantReadWriteLock();

  private T[] values;

  ReentrantLocked(T[] values) {
    this.values = values;
  }

  @Override
  public final Values<T> push(final T value) {
    try (final var ignored = new WriteLock(reentrant)) {
      values = Arrays.copyOf(values, values.length + 1);
      values[values.length - 1] = value;
      return this;
    }
  }

  @Override
  public T pop() {
    try (final var ignored = new ReadLock(reentrant)) {
      if (values.length == 0) return null;
      final var value = values[values.length - 1];
      values = Arrays.copyOf(this.values, this.values.length - 1);
      return value;
    }
  }
}
