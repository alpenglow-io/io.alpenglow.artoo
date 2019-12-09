package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static dev.lug.oak.type.Nullability.nonNullableState;

public interface Single<T> extends Nullable<T>, Supplier1<T> {
  @NotNull
  @Contract("_ -> new")
  static <S> Single<S> of(final S value) {
    return new Some<>(value);
  }

  @Override
  default Single<T> or(final T value) {
    return new EitherSingle<>(this, value);
  }

  @Override
  default <E extends RuntimeException> Single<T> or(final String message, final Function1<? super String, ? extends E> exception) {
    return new EitherSingleException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }

  @NotNull
  @Override
  default Iterator<T> iterator() {
    return Cursor.of(get());
  }

  default void eventually(@NotNull final Consumer1<T> consumer) {
    nonNullable(consumer, "consumer").accept(get());
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

final class EitherSingle<T> implements Single<T> {
  private final Single<T> single;
  private final T otherwise;

  @Contract(pure = true)
  EitherSingle(final Single<T> single, final T otherwise) {
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

final class EitherSingleException<T, E extends RuntimeException> implements Single<T> {
  private final Single<T> single;
  private final String message;
  private final Function1<? super String, ? extends E> exception;

  @Contract(pure = true)
  EitherSingleException(final Single<T> single, final String message, final Function1<? super String, ? extends E> exception) {
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
