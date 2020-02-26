package oak.query.one;

import oak.cursor.Cursor;
import oak.func.Pred;
import oak.query.Queryable;
import oak.type.Nullability;

import static oak.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(Pred<? super T> filter) {
    return Nullability.nonNullable(filter, f -> () -> {
      final var value = this.iterator().next();
      return nonNull(value) && filter.apply(value) ? Cursor.all(value) : Cursor.none();
    });
  }

  default <R> One<R> ofType(Class<? extends R> type) {
    return Nullability.nonNullable(type, t -> () -> {
      final var value = this.iterator().next();
      return nonNull(value) && t.isInstance(value) ? Cursor.all(t.cast(value)) : Cursor.none();
    });
  }
}
