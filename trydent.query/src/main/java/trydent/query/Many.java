package trydent.query;

import trydent.func.Suppl;
import trydent.query.many.Aggregatable;
import trydent.query.many.Concatenatable;
import trydent.query.many.Default;
import trydent.query.many.Filterable;
import trydent.query.many.Groupable;
import trydent.query.many.Insertable;
import trydent.query.many.Joinable;
import trydent.query.many.Partitionable;
import trydent.query.many.Projectable;
import trydent.query.many.Quantifiable;
import trydent.query.many.Settable;
import trydent.query.many.Uniquable;
import trydent.query.many.internal.Array;
import trydent.query.many.internal.Iteration;
import trydent.query.many.internal.Repeat;
import trydent.query.one.Either;
import trydent.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
