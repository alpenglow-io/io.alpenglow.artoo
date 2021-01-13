package io.artoo.lance.scope;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

import static io.artoo.lance.func.Func.Default.Nothing;

public interface Let<T> {
  static <T> Let<T> lazy(final Suppl.Uni<T> supplier) {
    return new Let.Lazy<>(supplier);
  }

  <R> R let(final Func.Uni<? super T, ? extends R> func);
  default Let<T> get(final Cons.Uni<? super T> func) {
    let(it -> {
      func.accept(it);
      return null;
    });
    return this;
  }

  sealed interface Readonly<T> extends Let<T> permits Let.Lazy {}

  final class Lazy<T> implements Readonly<T> {
    private final Suppl.Uni<? extends T> suppl;
    private volatile Object value;

    private Lazy(final Suppl.Uni<T> suppl) {
      this(
        suppl,
        Nothing
      );
    }

    private Lazy(final Suppl.Uni<? extends T> suppl, final Object value) {
      this.suppl = suppl;
      this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R let(final Func.Uni<? super T, ? extends R> func) {
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
