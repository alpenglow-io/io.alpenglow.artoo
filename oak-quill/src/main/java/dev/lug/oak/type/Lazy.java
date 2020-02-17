package dev.lug.oak.type;

import dev.lug.oak.func.Sup;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Emptyness.Nothing;

@SuppressWarnings("UnusedReturnValue")
public interface Lazy<T> extends Sup<T> {
  @NotNull
  @Contract(value = "_ -> new", pure = true)
  static <V> Lazy<V> of(final Sup<V> supplier) {
    return new SyncLazy<>(supplier);
  }

  @NotNull
  @Contract(pure = true)
  static <V> Lazy<V> lazy(final Sup<V> supplier) {
    return of(supplier);
  }

  Lazy<T> release();
  Lazy<T> attach(final Sup<T> attacher);
}

enum Emptyness {Nothing}

final class SyncLazy<T> implements Lazy<T> {
  private volatile Sup<T> supplier;
  private volatile Object value;

  @Contract(pure = true)
  SyncLazy(final Sup<T> supplier) {
    this(
      supplier,
      Nothing
    );
  }
  @Contract(pure = true)
  private SyncLazy(final Sup<T> supplier, final Object value) {
    this.supplier = supplier;
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final T get() {
    final var v1 = value;
    if (!v1.equals(Nothing))
      return (T) v1;
    else synchronized (this) {
      final var v2 = value;
      return !v2.equals(Nothing) ? (T) v1 : set();
    }
  }

  private T set() {
    final var initialized = supplier.get();
    this.value = initialized;
    return initialized;
  }

  @Contract(" -> this")
  private Lazy<T> unset() {
    this.value = Nothing;
    return this;
  }

  @Override
  public Lazy<T> release() {
    final var v1 = value;
    if (v1.equals(Nothing))
      return this;
    else synchronized (this) {
      return value.equals(Nothing) ? this : unset();
    }
  }

  @Override
  @Contract("_ -> this")
  public Lazy<T> attach(final Sup<T> supplier) {
    synchronized (this) {
      this.supplier = supplier;
      return this;
    }
  }
}
