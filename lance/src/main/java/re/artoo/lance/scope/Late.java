package re.artoo.lance.scope;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;

import static re.artoo.lance.scope.Default.Nothing;

public non-sealed interface Late<T> extends Let<T> {
  static <T> Late<T> init() {
    return new Late.Init<>();
  }

  Late<T> set(TrySupplier1<? extends T> suppl);

  sealed interface WriteOnce<T> extends Late<T> permits Late.Init {}

  @SuppressWarnings("unchecked")
  final class Init<T> implements WriteOnce<T> {
    private volatile Object value;

    private Init() {
      this.value = Nothing;
    }

    @Override
    public Late<T> set(final TrySupplier1<? extends T> suppl) {
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
    public <R> R let(final TryFunction1<? super T, ? extends R> func) {
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
