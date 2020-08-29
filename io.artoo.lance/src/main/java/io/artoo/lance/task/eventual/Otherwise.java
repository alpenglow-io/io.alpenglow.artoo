package io.artoo.lance.task.eventual;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.task.Eventual;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.task.Operable;

public interface Otherwise<T> extends Operable<T> {
  default Eventual<T> or(final T element) {
    return or(Eventual.aid(element));
  }

  default <E extends Eventual<T>> Eventual<T> or(final E otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> Eventual<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> Eventual<T> or(final Suppl.Uni<? extends E> exception) {
    return or(null, it -> exception.tryGet());
  }
}
