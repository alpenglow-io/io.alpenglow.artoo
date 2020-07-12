package io.artoo.lance.type;

import io.artoo.lance.func.Suppl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static io.artoo.lance.func.Func.Nothing.Nil;

@SuppressWarnings("UnusedReturnValue")
public interface Lazy<T> extends Suppl.Uni<T> {
  static <V> Lazy<V> of(final Suppl.Uni<V> supplier) {
    return new SyncLazy<>(supplier);
  }

  static <V> Lazy<V> lazy(final Suppl.Uni<V> supplier) {
    return of(supplier);
  }

  Lazy<T> release();

  Lazy<T> attach(final Suppl.Uni<T> attacher);
}

final class SyncLazy<T> implements Lazy<T> {
  private volatile Suppl.Uni<T> supplier;
  private volatile Object value;

  SyncLazy(final Suppl.Uni<T> supplier) {
    this(
      supplier,
      Nil
    );
  }

  private SyncLazy(final Suppl.Uni<T> supplier, final Object value) {
    this.supplier = supplier;
    this.value = value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final T tryGet() {
    final var v1 = value;
    if (!v1.equals(Nil))
      return (T) v1;
    else
      synchronized (this) {
        final var v2 = value;
        return !v2.equals(Nil) ? (T) v1 : set();
      }
  }

  private T set() {
    final var initialized = supplier.get();
    this.value = initialized;
    return initialized;
  }

  @Contract(" -> this")
  private Lazy<T> unset() {
    this.value = Nil;
    return this;
  }

  @Override
  public Lazy<T> release() {
    final var v1 = value;
    if (v1.equals(Nil))
      return this;
    else
      synchronized (this) {
        return value.equals(Nil) ? this : unset();
      }
  }

  @Override
  public Lazy<T> attach(final Suppl.Uni<T> supplier) {
    synchronized (this) {
      this.supplier = supplier;
      return this;
    }
  }
}
