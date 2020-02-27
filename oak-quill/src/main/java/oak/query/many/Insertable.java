package oak.query.many;

import oak.query.Many;
import oak.query.Queryable;

import static oak.func.$2.IntCons.nothing;
import static oak.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, nothing(), nonNullable(values, "values"));
  }
}
