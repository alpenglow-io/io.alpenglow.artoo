package trydent.query.many;

import trydent.func.$2.ConsInt;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.many.internal.Insert;

import static java.util.Arrays.copyOf;
import static trydent.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, ConsInt.nothing(), nonNullable(values, "values"))::iterator;
  }
}
