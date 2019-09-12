package oak.quill.single;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.func.sup.Supplier1;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.func.fun.Function1.identity;
import static oak.type.Nullability.nonNullable;

public interface Casing<T> extends Structable<T> {
  default T or(final T value) {
    return new Or<>(this, nonNullable(value, "value")).get();
  }
  default <E extends RuntimeException> Nullable<T> or(final String message, final Function1<String, E> exception) {
    return new OrException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }
}

final class Or<T> implements Supplier1<T> {
  private final Structable<T> structable;
  private final T otherwise;

  @Contract(pure = true)
  Or(final Structable<T> structable, final T otherwise) {
    this.structable = structable;
    this.otherwise = otherwise;
  }

  @Override
  @Contract(pure = true)
  public final T get() {
    T result = null;
    for (final var value : structable) result = value;
    return result != null ? result : otherwise;
  }
}

final class OrException<T, E extends RuntimeException> implements Nullable<T> {
  private final Structable<T> structable;
  private final String message;
  private final Function1<String, E> exception;

  @Contract(pure = true)
  OrException(final Structable<T> structable, final String message, final Function1<String, E> exception) {
    this.structable = structable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = structable.iterator();
    if (!cursor.hasNext()) throw exception.apply(message);
    return cursor;
  }
}
