package artoo.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.Suppl;
import artoo.query.many.Aggregatable;
import artoo.query.many.Concatenatable;
import artoo.query.many.Default;
import artoo.query.many.Either;
import artoo.query.many.Filterable;
import artoo.query.many.Groupable;
import artoo.query.many.Insertable;
import artoo.query.many.Joinable;
import artoo.query.many.Partitionable;
import artoo.query.many.Projectable;
import artoo.query.many.Quantifiable;
import artoo.query.many.Settable;
import artoo.query.many.Uniquable;
import artoo.query.many.impl.Array;
import artoo.query.many.impl.Iteration;
import artoo.query.many.impl.Repeat;
import artoo.type.Nullability;

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
