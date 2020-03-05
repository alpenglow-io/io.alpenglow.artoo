package oak.query;

import oak.func.Suppl;
import oak.query.many.Aggregatable;
import oak.query.many.Concatenatable;
import oak.query.many.Default;
import oak.query.many.Filterable;
import oak.query.many.Groupable;
import oak.query.many.Insertable;
import oak.query.many.Joinable;
import oak.query.many.Partitionable;
import oak.query.many.Projectable;
import oak.query.many.Quantifiable;
import oak.query.many.Settable;
import oak.query.many.Uniquable;
import oak.query.many.internal.Array;
import oak.query.many.internal.Iteration;
import oak.query.many.internal.Repeat;
import oak.query.one.Either;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static oak.type.Nullability.nonNullable;

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
