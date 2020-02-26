package oak.query.many;

import oak.query.Many;
import oak.query.Queryable;

import java.util.ArrayList;
import java.util.Collections;

import static oak.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    nonNullable(values, "values");
    return () -> {
      final var array = new ArrayList<T>();
      for (final var element : this) array.add(element);
      Collections.addAll(array, values);
      return array.iterator();
    };
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    nonNullable(queryable, "queryable");
    return () -> {
      final var array = new ArrayList<T>();
      for (final var element : queryable) array.add(element);
      for (final var element : this) array.add(element);
      return array.iterator();
    };
  }
}
