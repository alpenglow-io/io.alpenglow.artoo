package lance.scope;

import lance.func.Cons;
import lance.func.Func;
import lance.func.Suppl;


import static lance.func.Func.Default.Nothing;

public interface Let<T> {
  static <T> Let<T> lazy(final Suppl.MaybeSupplier<T> supplier) {
    return new Let.Lazy<>(supplier);
  }

  <R> R let(final Func.TryFunction<? super T, ? extends R> func);
  default Let<T> get(final Cons.TryConsumer<? super T> func) {
    let(it -> {
      func.accept(it);
      return null;
    });
    return this;
  }

  sealed interface Readonly<T> extends Let<T> permits Let.Lazy {}

  final class Lazy<T> implements Readonly<T> {
    private final Suppl.MaybeSupplier<? extends T> suppl;
    private volatile Object value;

    private Lazy(final Suppl.MaybeSupplier<T> suppl) {
      this(
        suppl,
        Nothing
      );
    }

    private Lazy(final Suppl.MaybeSupplier<? extends T> suppl, final Object value) {
      this.suppl = suppl;
      this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R let(final Func.TryFunction<? super T, ? extends R> func) {
      final var unsyncd = value;
      if (Nothing.equals(unsyncd)) {
        synchronized (this) {
          final var syncd = value;
          if (Nothing.equals(syncd)) {
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
  }
}
