package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.$2.IntPre;
import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;

import java.util.ArrayList;

import static oak.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.apply(param));
  }

  default Many<T> where(final IntPre<? super T> where) {
    return where(where, IntFunc.identity());
  }

  default <C> Many<C> ofType(final Class<? extends C> type) {
    return where((index, it) -> type.isInstance(it), (index, value) -> type.cast(value));
  }

  default <R> Many<R> where(final IntPre<? super T> where, final IntFunc<? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return () -> {
      final var result = new ArrayList<R>();
      var index = 0;
      for (final var cursor = this.iterator(); cursor.hasNext(); index++) {
        final var value = cursor.next();
        if (value != null && where.verify(index, value)) {
          result.add(select.applyInt(index, value));
        }
      }
      return result.iterator();
    };
  }
}
