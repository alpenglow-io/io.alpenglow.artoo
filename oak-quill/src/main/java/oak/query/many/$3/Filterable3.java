package oak.query.many.$3;

import oak.func.$3.Fun;
import oak.func.$3.Pre;
import oak.query.Queryable;
import oak.query.$3.Queryable3;
import oak.query.Tuple;
import dev.lug.oak.query.tuple3.Tuple3;
import oak.query.many.Filterable;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Filterable3<V1, V2, V3> extends Filterable<Tuple3<V1, V2, V3>> {
  default Queryable3<V1, V2, V3> where(final Pre<? super V1, ? super V2, ? super V3> filter) {
    return new Where3<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default <T1, T2, T3> Queryable3<T1, T2, T3> ofType(final Class<T1> type1, final Class<T2> type2, final Class<T3> type3) {
    return new OfType3<>(this, Nullability.nonNullable(type1, "type1"), Nullability.nonNullable(type2, "type2"), Nullability.nonNullable(type3, "type3"));
  }
}

final class Where3<V1, V2, V3> implements Queryable3<V1, V2, V3> {
  private final Queryable<Tuple3<V1, V2, V3>> queryable;
  private final Pre<? super V1, ? super V2, ? super V3> filter;

  @Contract(pure = true)
  Where3(final Queryable<Tuple3<V1, V2, V3>> queryable, final Pre<? super V1, ? super V2, ? super V3> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<V1, V2, V3>> iterator() {
    final var many = new ArrayList<Tuple3<V1, V2, V3>>();
    for (final var tuple3 : queryable) {
      tuple3
        .where(filter)
        .peek((v1, v2, v3) -> many.add(Tuple.of(v1, v2, v3)));
    }
    return many.iterator();
  }
}

final class OfType3<V1, V2, V3, T1, T2, T3> implements Queryable3<T1, T2, T3> {
  @SuppressWarnings("unchecked")
  private final Fun<V1, V2, V3, @NotNull Tuple3<T1, T2, T3>> asTuple = (v1, v2, v3) -> Tuple.of((T1) v1, (T2) v2, (T3) v3);

  private final Queryable<Tuple3<V1, V2, V3>> queryable;
  private final Pre<? super V1, ? super V2, ? super V3> areTypes;

  @Contract(pure = true)
  OfType3(final Queryable<Tuple3<V1, V2, V3>> queryable, final Class<T1> type1, final Class<T2> type2, final Class<T3> type3) {
    this(
      queryable,
      (v1, v2, v3) -> type1.isInstance(v1) && type2.isInstance(v2) && type3.isInstance(v3)
    );
  }
  @Contract(pure = true)
  private OfType3(final Queryable<Tuple3<V1, V2, V3>> queryable, final Pre<? super V1, ? super V2, ? super V3> areTypes) {
    this.queryable = queryable;
    this.areTypes = areTypes;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = new ArrayList<Tuple3<T1, T2, T3>>();
    for (final var tuple3 : queryable) {
      tuple3
        .where(areTypes)
        .selection(asTuple)
        .forEach(result::add);
    }
    return result.iterator();
  }
}
