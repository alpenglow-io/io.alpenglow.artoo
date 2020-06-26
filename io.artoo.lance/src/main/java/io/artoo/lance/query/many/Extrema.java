package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
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
    return new Extremum<T, N, V>(this, it -> {}, type, nonNullable(select, "select"));
  }
}

@SuppressWarnings("unchecked")
final class Extremum<T, N extends Number, V> implements One<V> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final int extreme;
  private final Func.Uni<? super T, ? extends N> number;

  Extremum(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final int extreme, final Func.Uni<? super T, ? extends N> number) {
    assert queryable != null && peek != null && number != null;
    this.queryable = queryable;
    this.peek = peek;
    this.extreme = extreme;
    this.number = number;
  }

  @NotNull
  @Override
  public final Cursor<V> cursor() {
    N compared = null;
    for (final var it : queryable) {
      if (it != null) {
        peek.accept(it);
        final var numbered = number.apply(it);
        if (compared == null || numbered != null && compare(compared, numbered) == extreme) {
          compared = numbered;
        }
      }
    }
    return Cursor.of((V) compared);
  }

  private int compare(final N compared, final N numbered) {
    if (numbered instanceof Byte b) {
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
