package dev.lug.oak.quill.query.tuple;

import dev.lug.oak.collect.Many;
import dev.lug.oak.quill.query.Filterable;
import dev.lug.oak.quill.tuple.Tuple;
import dev.lug.oak.quill.tuple.Tuple2;
import dev.lug.oak.type.Nullability;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.func.pre.Predicate2;
import dev.lug.oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Filterable2<V1, V2> extends Filterable<Tuple2<V1, V2>> {
  default Queryable2<V1, V2> where(final Predicate2<? super V1, ? super V2> filter) {
    return new Where2<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default <T1, T2> Queryable2<T1, T2> ofType(final Class<T1> type1, final Class<T2> type2) {
    return new OfType2<>(this, Nullability.nonNullable(type1, "type1"), Nullability.nonNullable(type2, "type2"));
  }
}

final class Where2<V1, V2> implements Queryable2<V1, V2> {
  private final Structable<Tuple2<V1, V2>> structable;
  private final Predicate2<? super V1, ? super V2> filter;

  @Contract(pure = true)
  Where2(final Structable<Tuple2<V1, V2>> structable, final Predicate2<? super V1, ? super V2> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<V1, V2>> iterator() {
    final var many = Many.<Tuple2<V1, V2>>of();
    for (final var tuple2 : structable) {
      tuple2
        .where(filter)
        .peek((v1, v2) -> many.add(Tuple.of(v1, v2)));
    }
    return many.iterator();
  }
}

final class OfType2<V1, V2, T1, T2> implements Queryable2<T1, T2> {
  @SuppressWarnings("unchecked")
  private final Function2<V1, V2, @NotNull Tuple2<T1, T2>> asTuple = (v1, v2) -> Tuple.of((T1) v1, (T2) v2);

  private final Structable<Tuple2<V1, V2>> structable;
  private final Predicate2<? super V1, ? super V2> areTypes;

  @Contract(pure = true)
  OfType2(final Structable<Tuple2<V1, V2>> structable, final Class<T1> type1, final Class<T2> type2) {
    this(
      structable,
      (v1, v2) -> type1.isInstance(v1) && type2.isInstance(v2)
    );
  }
  @Contract(pure = true)
  private OfType2(final Structable<Tuple2<V1, V2>> structable, final Predicate2<? super V1, ? super V2> areTypes) {
    this.structable = structable;
    this.areTypes = areTypes;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = Many.<Tuple2<T1, T2>>of();
    for (final var tuple2 : structable) {
      tuple2
        .where(areTypes)
        .selection(asTuple)
        .forEach(result::add);
    }
    return result.iterator();
  }
}
