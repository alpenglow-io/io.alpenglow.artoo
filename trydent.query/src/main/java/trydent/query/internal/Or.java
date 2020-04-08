package trydent.query.internal;

import trydent.func.$2.ConsInt;
import trydent.func.Func;
import trydent.func.Suppl;
import trydent.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Or<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final Queryable<T> otherwise;
  private final ConsInt<? super T> peek;
  private final String message;
  private final Func<? super String, ? extends RuntimeException> exception;

  public Or(final Queryable<T> queryable, final Queryable<T> otherwise) {
    this(queryable, null, otherwise, null, null);
  }
  public Or(final Queryable<T> queryable, final Suppl<? extends RuntimeException> exception) {
    this(queryable, null, null, message -> exception.get());
  }
  public Or(final Queryable<T> queryable, final String message, final Func<? super String, ? extends RuntimeException> exception) {
    this(queryable, null, null, message, exception);
  }

  public Or(final Queryable<T> queryable, ConsInt<? super T> peek, final Queryable<T> otherwise) {
    this(queryable, peek, otherwise, null, null);
  }
  public Or(final Queryable<T> queryable, ConsInt<? super T> peek, final Suppl<? extends RuntimeException> exception) {
    this(queryable, peek, null, message -> exception.get());
  }
  public Or(final Queryable<T> queryable, ConsInt<? super T> peek, final String message, final Func<? super String, ? extends RuntimeException> exception) {
    this(queryable, peek, null, message, exception);
  }
  private Or(final Queryable<T> queryable, ConsInt<? super T> peek, final Queryable<T> otherwise, final String message, final Func<? super String, ? extends RuntimeException> exception) {
    this.queryable = queryable;
    this.peek = peek;
    this.otherwise = otherwise;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.iterator();
    if (cursor.hasNext()) return queryable.iterator();

    final var except = exception.apply(message);
    if (except == null) return otherwise.iterator();

    throw except;
  }
}
