package io.artoo.lance.type;

import io.artoo.lance.func.Suppl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static io.artoo.lance.func.Func.Default.Nothing;
import static io.artoo.lance.type.Nullability.nonNullable;

public sealed interface Value<T> extends Suppl.Uni<T> permits Late, Absent, Lazy {
  static <T> Value<T> late() {
    return new Late<>();
  }

  @SuppressWarnings("unchecked")
  static <T> Value<T> absent() {
    return (Value<T>) Absent.Default;
  }

  static <T> Value<T> lazy(final Suppl.Uni<T> supplier) {
    return new Lazy<>(supplier);
  }

  default Value<T> set(T value) { return this; }
  default Value<T> set(Suppl.Uni<T> value) { return set(value.get()); }

  @Override
  default T tryGet() { return null; }
}

enum Absent implements Value<Object> {Default}

@SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
final class Late<T> implements Value<T> {
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private T value;

  Late() { this(null); }
  Late(final T value) {
    this.value = value;
  }

  @Override
  public final T tryGet() {
    final var read = this.lock.readLock();
    try {
      read.lock();
      return this.value;
    } finally {
      read.unlock();
    }
  }

  @Override
  public final Value<T> set(final T value) {
    final var write = this.lock.writeLock();
    try {
      write.lock();
      this.value = value;
    } finally {
      write.unlock();
    }
    return this;
  }

  @Override
  public Value<T> set(final Suppl.Uni<T> value) {
    final var write = this.lock.writeLock();
    try {
      write.lock();
      this.value = value.tryGet();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    } finally {
      write.unlock();
    }
    return this;
  }

  @Override
  public final String toString() {
    return value.toString();
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final boolean equals(final Object obj) {
    return value.equals(obj);
  }
}

final class Lazy<T> implements Value<T> {
  private volatile Suppl.Uni<T> supplier;
  private volatile Object value;

  Lazy(final Suppl.Uni<T> supplier) {
    this(
      supplier,
      Nothing
    );
  }
  private Lazy(final Suppl.Uni<T> supplier, final Object value) {
    this.supplier = supplier;
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final T tryGet() {
    final var v1 = value;
    if (!v1.equals(Nothing))
      return (T) v1;
    else
      synchronized (this) {
        final var v2 = value;
        return !v2.equals(Nothing) ? (T) v1 : set();
      }
  }

  private T set() {
    final var initialized = supplier.get();
    this.value = initialized;
    return initialized;
  }

  @Override
  public final Value<T> set(final T value) {
    synchronized (this) {
      this.supplier = () -> value;
      return this;
    }
  }

  @Override
  public Value<T> set(final Suppl.Uni<T> value) {
    synchronized (this) {
      this.supplier = value;
      return this;
    }
  }
}
