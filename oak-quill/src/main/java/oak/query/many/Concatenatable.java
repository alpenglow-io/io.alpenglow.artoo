package oak.query.many;

import oak.query.Many;
import oak.query.Queryable;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    nonNullable(queryable, "queryable");
    return () -> {
      final var array = new ArrayList<T>();
      for (final var value : this) array.add(value);
      for (final var value : queryable) array.add(value);
      return array.iterator();
    };
  }
}
