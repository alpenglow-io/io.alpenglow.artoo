package lance.query.one;

import lance.func.Func;
import lance.func.Suppl;
import lance.query.One;
import lance.Queryable;

public interface Elseable<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.maybe(element));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.TryBiFunction<? super Throwable, ? super String, ? extends E> exception) {
    return () -> cursor().or(message, (m, c) -> exception.tryApply(c, m));
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.TryFunction<? super String, ? extends E> exception) {
    return () -> cursor().or(message, (msg, cause) -> exception.tryApply(msg));
  }

  default <E extends RuntimeException> One<T> or(final Suppl.MaybeSupplier<? extends E> exception) {
    return or(null, (it, throwable) -> exception.tryGet());
  }

  default T otherwise(final T other) {
    return or(other).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final String message, final Func.TryBiFunction<? super Throwable, ? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final Suppl.MaybeSupplier<? extends E> exception) {
    return or(exception).iterator().next();
  }
}
