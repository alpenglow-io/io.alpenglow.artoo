package oak.quill.single;

import oak.func.fun.Function1;
import oak.func.sup.Supplier1;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNullElse;
import static oak.type.Nullability.nonNullable;

public interface Casing<T> extends Structable<T> {
  default Single<T> or(final T value) {
    return new Or<>(this, nonNullable(value, "value"));
  }
  default <E extends RuntimeException> Single<T> or(final String message, final Function1<String, E> exception) {
    return new OrException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }
}

final class Or<T> implements Single<T> {
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
    return requireNonNullElse(structable.iterator().next(), otherwise);
  }
}

final class OrException<T, E extends RuntimeException> implements Single<T> {
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
  public final T get() {
    final var value = structable.iterator().next();
    if (value == null) throw exception.apply(message);
    return value;
  }
}
