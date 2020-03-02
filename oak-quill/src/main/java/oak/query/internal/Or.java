package oak.query.internal;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.func.Func;
import oak.func.Suppl;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Or<T, E extends RuntimeException> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final Queryable<T> otherwise;
  private final IntCons<? super T> peek;
  private final String message;
  private final Func<? super String, ? extends E> exception;

  public Or(final Queryable<T> queryable, IntCons<? super T> peek, final Queryable<T> otherwise) {
    this(queryable, peek, otherwise, null, null);
  }
  public Or(final Queryable<T> queryable, IntCons<? super T> peek, final Suppl<? extends E> exception) {
    this(queryable, peek, null, message -> exception.get());
  }
  public Or(final Queryable<T> queryable, IntCons<? super T> peek, final String message, final Func<? super String, ? extends E> exception) {
    this(queryable, peek, null, message, exception);
  }
  private Or(final Queryable<T> queryable, IntCons<? super T> peek, final Queryable<T> otherwise, final String message, final Func<? super String, ? extends E> exception) {
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
