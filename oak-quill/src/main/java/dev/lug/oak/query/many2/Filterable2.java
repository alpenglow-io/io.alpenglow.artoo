package dev.lug.oak.query.many2;

import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.pre.Predicate2;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.Tuple;
import dev.lug.oak.query.Tuple2;
import dev.lug.oak.query.many.Filterable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Filterable2<V1, V2> extends Filterable<Tuple2<V1, V2>> {
  default Many2<V1, V2> where(final Predicate2<? super V1, ? super V2> filter) {
    return new Where2<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default <T1, T2> Many2<T1, T2> ofType(final Class<T1> type1, final Class<T2> type2) {
    return new OfType2<>(this, Nullability.nonNullable(type1, "type1"), Nullability.nonNullable(type2, "type2"));
  }
}

final class Where2<V1, V2> implements Many2<V1, V2> {
  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Predicate2<? super V1, ? super V2> filter;

  @Contract(pure = true)
  Where2(final Queryable<Tuple2<V1, V2>> queryable, final Predicate2<? super V1, ? super V2> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    final var many = new ArrayList<Tuple2<V1, V2>>();
    for (final var tuple2 : queryable) {
      tuple2
        .where(filter)
        .peek((v1, v2) -> many.add(Tuple.of(v1, v2)));
    }
    return many.iterator();
  }
}

final class OfType2<V1, V2, T1, T2> implements Many2<T1, T2> {
  @SuppressWarnings("unchecked")
  private final Function2<V1, V2, @NotNull Tuple2<T1, T2>> asTuple = (v1, v2) -> Tuple.of((T1) v1, (T2) v2);

  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Predicate2<? super V1, ? super V2> areTypes;

  @Contract(pure = true)
  OfType2(final Queryable<Tuple2<V1, V2>> queryable, final Class<T1> type1, final Class<T2> type2) {
    this(
      queryable,
      (v1, v2) -> type1.isInstance(v1) && type2.isInstance(v2)
    );
  }
  @Contract(pure = true)
  private OfType2(final Queryable<Tuple2<V1, V2>> queryable, final Predicate2<? super V1, ? super V2> areTypes) {
    this.queryable = queryable;
    this.areTypes = areTypes;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var tuple2 : queryable) {
      tuple2
        .where(areTypes)
        .select(asTuple)
        .forEach(result::add);
    }
    return result.iterator();
  }
}
