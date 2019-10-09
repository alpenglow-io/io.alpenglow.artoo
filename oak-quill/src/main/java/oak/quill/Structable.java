package oak.quill;

import oak.quill.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Structable<T> extends Iterable<T> {
  @NotNull
  @Contract(pure = true)
  static <R> Queryable<R> asQueryable(final Structable<R> structable) {
    return () -> nonNullable(structable, "structable").iterator();
  }
/*
  @NotNull
  @Contract(pure = true)
  static <R> Nullable<R> asNullable(final Structable<R> structable) {
    return () -> nonNullable(structable, "structable").iterator().next();
  }

  @NotNull
  @Contract(pure = true)
  static <R> Single<R> asSingle(final Structable<R> structable) {
    return () ->
      nonNullableState(
        nonNullable(structable, "structable").iterator(),
        "cursor"
      ).next();
  }*/
}
