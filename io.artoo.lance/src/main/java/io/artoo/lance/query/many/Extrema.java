package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Extrema<T> extends Queryable<T> {

  default <N extends Number> One<N> max(final Func.Uni<? super T, ? extends N> select) {
    return extreme(1, select);
  }

  default One<T> max() {
    return extreme(1);
  }

  default <N extends Number> One<N> min(final Func.Uni<? super T, ? extends N> select) {
    return extreme(-1, select);
  }

  default One<T> min() {
    return extreme(-1);
  }

  private One<T> extreme(int type) {
    return this.extreme(type, it -> it instanceof Number n ? n : null);
  }

  private <N extends Number, V> One<V> extreme(int type, final Func.Uni<? super T, ? extends N> select) {
    return new Extremum<T, N, V>(this, type, nonNullable(select, "select"));
  }
}

@SuppressWarnings("unchecked")
final class Extremum<T, N extends Number, V> implements One<V> {
  private final Queryable<T> queryable;
  private final int extreme;
  private final Func.Uni<? super T, ? extends N> number;

  Extremum(final Queryable<T> queryable, final int extreme, final Func.Uni<? super T, ? extends N> number) {
    assert queryable != null && number != null;
    this.queryable = queryable;
    this.extreme = extreme;
    this.number = number;
  }

  @NotNull
  @Override
  public final Cursor<V> cursor() {
    final var compared = Cursor.<V>local();

    final var cursor = queryable.cursor();
    while (cursor.hasNext() && !compared.hasCause()) {
      try {
        final var numbered = cursor.fetch(number::tryApply);

        final var element = compared.next();
        if (compare((N) element, numbered) == extreme) {
          compared.set((V) numbered);
        } else {
          compared.set(element);
        }

      } catch (Throwable cause) {
        compared.grab(cause);
      }
    }

    return compared;
  }

  private int compare(final N compared, final N numbered) {
    if (compared == null && numbered != null) {
      return extreme;

    } else if (numbered instanceof Byte b) {
      return b > compared.byteValue() ? 1 : -1;

    } else if (numbered instanceof Short s) {
      return s > compared.shortValue() ? 1 : -1;

    } else if (numbered instanceof Integer i) {
      return i > compared.intValue() ? 1 : -1;

    } else if (numbered instanceof Long l) {
      return l > compared.longValue() ? 1 : -1;

    } else if (numbered instanceof Float f) {
      return f > compared.floatValue() ? 1 : -1;

    } else if (numbered instanceof Double d) {
      return d > compared.doubleValue() ? 1 : -1;

    } else if (numbered instanceof BigInteger it) {
      return it.longValue() > BigInteger.valueOf(compared.longValue()).longValue() ? 1 : -1;

    } else if (numbered instanceof BigDecimal it) {
      return it.doubleValue() > BigDecimal.valueOf(compared.doubleValue()).doubleValue() ? 1 : -1;
    }

    throw new IllegalStateException("Can't cast unknown number type");
  }
}
