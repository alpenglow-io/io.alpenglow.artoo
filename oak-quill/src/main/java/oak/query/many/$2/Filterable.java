package oak.query.many.$2;

import oak.union.$2.Union;
import oak.func.$2.Pre;
import oak.query.$2.Queryable;
import oak.type.Nullability;

import java.util.ArrayList;

import static oak.type.Nullability.nonNullable;

public interface Filterable<V1, V2> extends Queryable<V1, V2> {
  default Many<V1, V2> where(final Pre<? super V1, ? super V2> filter) {
    Nullability.nonNullable(filter, "filter");
    return () -> {
      final var array = new ArrayList<Union<V1, V2>>();
      for (final var tuple : this) if (tuple.as(filter::apply)) array.add(tuple);
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
