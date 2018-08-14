package oak.func;

import oak.func.fun.Function1;

import static java.util.Objects.requireNonNull;

public interface With<T> {
  static <S> With<S> on(final S any) {
    return new WithImpl<>(
      requireNonNull(any, "Any is null")
    );
  }

  <R> R then(final Function1<T, R> then);

  final class WithImpl<T> implements With<T> {
    private final T any;

    WithImpl(final T any) {
      this.any = any;
    }

    @Override
    public final <R> R then(final Function1<T, R> transform) {
      return requireNonNull(transform, "Transform is null").apply(any);
    }
  }
}
