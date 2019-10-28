package dev.lug.oak.quill;

import dev.lug.oak.quill.query.Queryable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Structable<T> extends Iterable<T> {
  @NotNull
  @Contract(pure = true)
  static <R> Queryable<R> asQueryable(final Structable<R> structable) {
    return () -> Nullability.nonNullable(structable, "structable").iterator();
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
