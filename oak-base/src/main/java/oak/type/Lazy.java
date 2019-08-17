package oak.type;

import oak.func.sup.Supplier1;

import static oak.type.Emptyness.None;

public interface Lazy<T> extends Supplier1<T> {
  static <V> Lazy<V> of(final Supplier1<V> initialization) {
    return new SyncLazy<>(initialization, None);
  }

  static <V> Lazy<V> lazy(final Supplier1<V> initialization) {
    return of(initialization);
  }
}

enum Emptyness { None }

final class SyncLazy<T> implements Lazy<T> {
  private final Supplier1<T> initialization;
  private volatile Object value;

  SyncLazy(final Supplier1<T> initialization, final Object initial) {
    this.initialization = initialization;
    this.value = initial;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final T get() {
    final var v1 = value;
    if (!v1.equals(None))
      return (T) v1;
    else synchronized (this) {
      final var v2 = value;
      return !v2.equals(None) ? (T) v1 : initialize();
    }
  }

  private T initialize() {
    final var initializated = initialization.get();
    this.value = initializated;
    return initializated;
  }
}
