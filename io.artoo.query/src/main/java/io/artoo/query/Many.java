package io.artoo.query;

import io.artoo.query.many.Aggregatable;
import io.artoo.query.many.Concatenatable;
import io.artoo.query.many.Default;
import io.artoo.query.many.Otherwise;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;

import static io.artoo.type.Nullability.nonNullable;

public interface Many<R extends Record> extends
  Projectable<R>, Filterable<R>, Partitionable<R>, Uniquable<R>, Aggregatable<R>, Concatenatable<R>, Groupable<R>,
  Joinable<R>, Quantifiable<R>, Settable<R>, Insertable<R>, Otherwise<R> {

  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <R extends Record> Many<R> from(final R... items) {
    return new Array<>(Arrays.copyOf(items, items.length));
  }

  @NotNull
  @Contract("_ -> new")
  static <R extends Record> Many<R> from(final Iterable<R> iterable) {
    return new Iteration<>(nonNullable(iterable, "iterable"));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <R extends Record> Many<R> none() {
    return (Many<R>) Default.None;
  }

  @NotNull
  @Contract("_, _ -> new")
  static <R extends Record> Many<R> repeat(final Supplier<? extends R> supplier, final int count) {
    return new Repeat<>(nonNullable(supplier, "supplier"), count);
  }
}
