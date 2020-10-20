package io.artoo.lance.type;

import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public sealed interface Values<T> permits Lockeds {
  @SafeVarargs
  static <T> Values<T> lock(T... values) {
    return new Lockeds<>(values);
  }

  static <T> Values<T> empty() {
    return lock();
  }

  Values<T> push(T value);
  T pop();
}

final class Lockeds<T> implements Values<T> {
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  private T[] values;

  Lockeds(T[] values) {
    this.values = values;
  }

  @Override
  public final Values<T> push(final T value) {
    final var write = this.lock.writeLock();
    try {
      write.lock();
      values = Arrays.copyOf(values, values.length + 1);
      values[values.length - 1] = value;
    } finally {
      write.unlock();
    }
    return this;
  }

  @Override
  public final T pop() {
    if (values.length == 0) return null;

    final var read = this.lock.readLock();
    try {
      read.lock();
      final var value = values[values.length - 1];
      values = Arrays.copyOf(this.values, this.values.length - 1);
      return value;
    } finally {
      read.unlock();
    }
  }
}
