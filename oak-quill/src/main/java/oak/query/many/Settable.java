package oak.query.many;

import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return new Distinct<>(this);
  }

  default Many<T> distinct(final Pred<? super T> filter) {
    return new Distinct<>(this, Nullability.nonNullable(filter, "filter"));
  }
}

final class Distinct<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Pred<? super T> filter;

  @Contract(pure = true)
  Distinct(final Queryable<T> queryable) {
    this(
      queryable,
      tautology()
    );
  }
  @Contract(pure = true)
  Distinct(final Queryable<T> queryable, final Pred<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    for (final var value : queryable) {
      if (nonNull(value) && filter.test(value) && !result.contains(value) || !filter.test(value))
        result.add(value);
    }
    return result.iterator();
  }
}
