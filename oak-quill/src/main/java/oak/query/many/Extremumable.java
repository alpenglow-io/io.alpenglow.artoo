package oak.query.many;

import oak.cursor.Cursor;
import oak.query.Queryable;
import oak.query.one.One;
import org.jetbrains.annotations.NotNull;

import static java.lang.Integer.compare;
import static oak.func.Func.identity;
import static oak.type.Nullability.nonNullable;

interface Extremumable<T> extends Queryable<T> {
  @NotNull
  static <T, R> oak.func.Func<? super T, Comparable<? super R>> comparing() {
    return mapped -> result -> compare(result.hashCode(), mapped.hashCode());
  }

  default <R> One<R> max(final oak.func.Func<? super T, ? extends R> select) {
    return this.<R>ext(select, comparing(), 1);
  }

  default One<T> max() {
    return this.<T>ext(identity(), comparing(), 1);
  }

  default <R> One<R> min(final oak.func.Func<? super T, ? extends R> select) {
    return this.<R>ext(select, comparing(), -1);
  }

  default One<T> min() {
    return this.<T>ext(identity(), comparing(), -1);
  }

  default <R> One<R> ext(final oak.func.Func<? super T, ? extends R> select, final oak.func.Func<? super R, Comparable<? super R>> where, final int ends) {
    //this.<R, R>aggregate(null, tautology(), select, (current, next) -> where.apply(next).compareTo(current) == ends ? next : current);
    nonNullable(select, "select");
    return () -> {
      R result = null;
      for (final var value : this) {
        if (value != null) {
          final var mapped = select.apply(value);
          if (result == null || mapped != null && where.apply(mapped).compareTo(result) == ends)
            result = mapped;
        }
      }
      return Cursor.ofNullable(result);
    };
  }
}
