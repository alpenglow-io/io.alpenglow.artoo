package io.artoo.lance.scope;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import static io.artoo.lance.func.Func.Default.Nothing;

public interface Late<T> extends Let<T> {
  static <T> Late<T> init() {
    return new Late.Init<>();
  }

  Late<T> set(Suppl.Uni<? extends T> suppl);

  sealed interface Writeonce<T> extends Late<T> permits Late.Init {}

  @SuppressWarnings("unchecked")
  final class Init<T> implements Writeonce<T> {
    private volatile Object value;

    private Init() {
      this.value = Nothing;
    }

    @Override
    public Late<T> set(final Suppl.Uni<? extends T> suppl) {
      final var unsyncd = this.value;
      if (Nothing.equals(unsyncd)) {
        synchronized (this) {
          final var syncd = this.value;
          if (Nothing.equals(syncd)) {
            final var supplied = suppl.get();
            if (supplied != null) this.value = supplied;
          }
        }
      }
      return this;
    }

    @Override
    public <R> R let(final Func.Uni<? super T, ? extends R> func) {
      final var unsyncd = this.value;
      if (Nothing.notEquals(unsyncd)) {
        synchronized (this) {
          final var syncd = this.value;
          if (Nothing.notEquals(syncd)) {
            return func.apply((T) syncd);
          }
        }
      }
      return func.apply(null);
    }
  }
}
