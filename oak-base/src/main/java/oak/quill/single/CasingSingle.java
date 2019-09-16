package oak.quill.single;

import oak.func.fun.Function1;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNullElse;
import static oak.type.Nullability.nonNullable;

public interface CasingSingle<T> extends StructableSingle<T> {
  default T or(final T value) {
    return new Or<>(this, nonNullable(value, "value")).get();
  }
  default <E extends RuntimeException> Nullable<T> or(final String message, final Function1<String, E> exception) {
    return new OrException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }
}

final class Or<T> implements Supplier1<T> {
  private final StructableSingle<T> structable;
  private final T otherwise;

  @Contract(pure = true)
  Or(final StructableSingle<T> structable, final T otherwise) {
    this.structable = structable;
    this.otherwise = otherwise;
  }

  @Override
  @Contract(pure = true)
  public final T get() {
    return requireNonNullElse(structable.get(), otherwise);
  }
}

final class OrException<T, E extends RuntimeException> implements Nullable<T> {
  private final StructableSingle<T> structable;
  private final String message;
  private final Function1<String, E> exception;

  @Contract(pure = true)
  OrException(final StructableSingle<T> structable, final String message, final Function1<String, E> exception) {
    this.structable = structable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final T get() {
    final var value = structable.get();
    if (value == null) throw exception.apply(message);
    return value;
  }
}
