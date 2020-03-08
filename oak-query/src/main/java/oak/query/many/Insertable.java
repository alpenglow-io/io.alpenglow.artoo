package oak.query.many;

import oak.func.$2.ConsInt;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.many.internal.Insert;

import static java.util.Arrays.copyOf;
import static oak.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, ConsInt.nothing(), nonNullable(values, "values"))::iterator;
  }
}
