package io.artoo.query.$2.many;

import io.artoo.func.$2.Pred;
import io.artoo.query.$2.Many;
import io.artoo.query.$2.Queryable;
import io.artoo.type.Nullability;
import io.artoo.union.$2.Union;

import java.util.ArrayList;

public interface Filterable<V1, V2> extends Queryable<V1, V2> {
  default Many<V1, V2> where(final Pred<? super V1, ? super V2> filter) {
    Nullability.nonNullable(filter, "filter");
    return () -> {
      final var array = new ArrayList<Union<V1, V2>>();
      for (final var tuple : this)
        if (tuple.as(filter::apply))
          array.add(tuple);
      return array.iterator();
    };
  }

  default <T1, T2> Many<T1, T2> ofTypes(final Class<T1> type1, final Class<T2> type2) {
    Nullability.nonNullable(type1, "type1");
    Nullability.nonNullable(type2, "type2");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : this)
        if (tuple.as((t1, t2) -> type1.isInstance(t1) && type2.isInstance(t2)))
          array.add(tuple.as((v1, v2) -> Union.of(type1.cast(v1), type2.cast(v2))));
      return array.iterator();
    };
  }
}
