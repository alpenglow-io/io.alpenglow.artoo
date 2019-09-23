package oak.quill.single;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullableState;

public interface Single<T> extends Nullable<T>, Supplier1<T> {
  @NotNull
  @Contract("_ -> new")
  static <S> Single<S> of(final S value) {
    return new Some<>(value);
  }

  @Override
  default T or(final T value) {
    return new OrSingle<>(this, value).get();
  }

  @Override
  default <E extends RuntimeException> T or(final String message, final Function1<String, E> exception) {
    return null;
  }

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.of(get());
  }
}

final class Some<T> implements Single<T> {
  private final T value;

  @Contract(pure = true)
  Some(final T value) {
    this.value = value;
  }

  @Contract(pure = true)
  @Override
  public final T get() {
    return nonNullableState(this.value, "Single");
  }
}

final class OrSingle<T> implements Single<T> {
  private final Single<T> single;
  private final T otherwise;

  @Contract(pure = true)
  OrSingle(final Single<T> single, final T otherwise) {
    this.single = single;
    this.otherwise = otherwise;
  }

  @Override
  public T get() {
    try {
      return single.get();
    } catch (IllegalStateException ise) {
      return otherwise;
    }
  }
}

final class OrSingleException<T, E extends RuntimeException> implements Single<T> {
  private final Single<T> single;
  private final String message;
  private final Function1<String, E> exception;

  @Contract(pure = true)
  OrSingleException(final Single<T> single, final String message, final Function1<String, E> exception) {
    this.single = single;
    this.message = message;
    this.exception = exception;
  }

  @org.jetbrains.annotations.Nullable
  @Contract(pure = true)
  @Override
  public final T get() {
    try {
      return single.get();
    } catch (IllegalStateException ise) {
      throw exception.apply(message);
    }
  }
}
