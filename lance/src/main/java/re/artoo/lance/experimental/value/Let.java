package re.artoo.lance.experimental.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;

public sealed interface Let<T> permits Late, Let.ReadOnly, Random {
  static <T> Let<T> lazy(final TrySupplier1<T> supplier) {
    return new Let.Lazy<>(supplier);
  }

  <R> R let(final TryFunction1<? super T, ? extends R> func);

  default Let<T> get(final TryConsumer1<? super T> func) {
    let(it -> {
      func.accept(it);
      return null;
    });
    return this;
  }

  default Let<T> flush() {
    return this;
  }

  sealed interface ReadOnly<T> extends Let<T> permits Let.Lazy {
  }

  final class Lazy<T> implements ReadOnly<T> {
    private final TrySupplier1<? extends T> suppl;
    private volatile Object value;

    private Lazy(final TrySupplier1<T> suppl) {
      this(
        suppl,
        Default.Nothing
      );
    }

    private Lazy(final TrySupplier1<? extends T> suppl, final Object value) {
      this.suppl = suppl;
      this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R let(final TryFunction1<? super T, ? extends R> func) {
      final var unsyncd = value;
      if (Default.Nothing.equals(unsyncd)) {
        synchronized (this) {
          final var syncd = value;
          if (Default.Nothing.equals(syncd)) {
            final var supplied = suppl.get();
            if (supplied != null)
              this.value = supplied;
            return func.apply(supplied);
          } else {
            return func.apply((T) syncd);
          }
        }
      } else {
        return func.apply((T) unsyncd);
      }
    }

    @Override
    public Let<T> flush() {
      final var unsyncd = value;
      if (!Default.Flushed.equals(unsyncd)) {
        synchronized (this) {
          final var syncd = value;
          if (!Default.Flushed.equals(syncd)) {
            this.value = Default.Flushed;
          }
        }
      }
      return this;
    }
  }
}
