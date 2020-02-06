package dev.lug.oak.query.tuple2;

import dev.lug.oak.collect.cursor.Cursor2;
import dev.lug.oak.func.pre.Predicate2;
import dev.lug.oak.query.Queryable2;
import dev.lug.oak.query.Tuple2;

public interface Filterable2<V1, V2> extends Queryable2<V1, V2> {
  default Tuple2<V1, V2> where(final Predicate2<? super V1, ? super V2> filter) {
    return () -> {
      for (final var tuple : this)
        if (tuple.as(filter::apply))
          return this.iterator();
      return Cursor2.none();
    };
  }
}
