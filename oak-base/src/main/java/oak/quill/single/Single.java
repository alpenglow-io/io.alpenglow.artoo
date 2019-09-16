package oak.quill.single;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static oak.type.Nullability.nonNullable;

public interface Single<T> extends Nullable<T> {
  @NotNull
  @Contract("_ -> new")
  static <S> Single<S> of(final S value) {
    return new Just<>(nonNullable(value, "value"));
  }
}

final class Just<T> implements Single<T> {
  private final T value;

  @Contract(pure = true)
  Just(final T value) {
    this.value = value;
  }

  @Override
  @Contract(pure = true)
  public final T get() {
    return value;
  }
}
