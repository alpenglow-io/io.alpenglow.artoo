package io.artoo.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.Suppl;
import io.artoo.query.many.Aggregatable;
import io.artoo.query.many.Concatenatable;
import io.artoo.query.many.Default;
import io.artoo.query.many.Either;
import io.artoo.query.many.Filterable;
import io.artoo.query.many.Groupable;
import io.artoo.query.many.Insertable;
import io.artoo.query.many.Joinable;
import io.artoo.query.many.Partitionable;
import io.artoo.query.many.Projectable;
import io.artoo.query.many.Quantifiable;
import io.artoo.query.many.Settable;
import io.artoo.query.many.Uniquable;
import io.artoo.query.many.impl.Array;
import io.artoo.query.many.impl.Iteration;
import io.artoo.query.many.impl.Repeat;
import io.artoo.type.Nullability;

import java.util.Arrays;

public interface Many<T> extends
  Projectable<T>, Filterable<T>, Partitionable<T>, Uniquable<T>, Aggregatable<T>, Concatenatable<T>, Groupable<T>,
  Joinable<T>, Quantifiable<T>, Settable<T>, Insertable<T>, Either<T> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T> Many<T> from(final T... items) {
    return new Array<>(Arrays.copyOf(items, items.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <T> Many<T> from(final Iterable<T> iterable) {
    return new Iteration<>(Nullability.nonNullable(iterable, "iterable"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <T> Many<T> none() {
    return (Many<T>) Default.None;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <S> Many<S> repeat(final Suppl<? extends S> supplier, final int count) {
    return new Repeat<>(Nullability.nonNullable(supplier, "supplier"), count);
  }
}
