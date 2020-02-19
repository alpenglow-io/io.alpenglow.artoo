package oak.query.many.$2;

import oak.union.$2.Union;
import oak.func.$2.Con;
import oak.func.$2.Func;
import oak.query.Many;
import oak.query.one.One;
import oak.query.Queryable.Tuple2AsAny;
import oak.query.Queryable.Tuple2AsMany;
import oak.query.Queryable.Tuple2AsTuple;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.query.Queryable.P.as;
import static oak.type.Nullability.nonNullable;

public interface Projectable2<V1, V2> extends oak.query.$2.Queryable<V1, V2> {
  default <R> Many<R> select(final Tuple2AsAny<? super V1, ? super V2, ? extends R> map) {
    Nullability.nonNullable(map, "map");
    return () -> {
      final var array = new ArrayList<R>();
      for (final var tuple : this)
        array.add(tuple.as(map::apply));
      return array.iterator();
    };
  }

  default <T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> Many2<T1, T2> select(final Tuple2AsTuple<? super V1, ? super V2, ? extends T> map) {
    Nullability.nonNullable(map, "map");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : this)
        tuple
          .as(map::apply)
          .select(as(Union::of))
          .eventually(array::add);
      return array.iterator();
    };
  }

  default <T1, T2, M extends Many2<T1, T2>> M select(final Tuple2AsMany<? super V1, ? super V2, ? extends M> flatMap) {
    Nullability.nonNullable(flatMap, "flatMap");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var tuple : this)
        tuple
          .as(flatMap::apply)
          .select(as(t -> t.select(as(Union::of))))
          .select(as(One::asIs))
          .eventually(t -> t.);
      return array.iterator();
    };
  }

  default Many2<V1, V2> peek2(final Con<? super V1, ? super V2> peek) {
    return new Peek2<>(this, Nullability.nonNullable(peek, "peek"));
  }
}

final class Select2AsAny<V1, V2, R> implements Many<R> {
  private final oak.query.$2.Queryable<V1, V2> queryable;
  private final Func<? super V1, ? super V2, ? extends R> map;

  @Contract(pure = true)
  Select2AsAny(final oak.query.$2.Queryable<V1, V2> queryable, final Func<? super V1, ? super V2, ? extends R> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var result = new ArrayList<R>();
    for (final var tuple2 : queryable) {
      tuple2
        .select(map)
        .forEach(result::add);
    }
    return result.iterator();
  }
}

final class Select2As2<V1, V2, T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> implements Many2<T1, T2> {
  private final oak.query.$2.Queryable<V1, V2> queryable;
  private final Func<? super V1, ? super V2, ? extends T> map;

  @Contract(pure = true)
  Select2As2(final oak.query.$2.Queryable<V1, V2> queryable, final Func<? super V1, ? super V2, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<Nominal2<T1, T2>> iterator() {
    final var result = new ArrayList<Nominal2<T1, T2>>();
    for (final var tuple : queryable) {
      map
        .apply(tuple.value1, tuple.value2)
        .select(as(Nominal2::new))
        .eventually(result::add);
    }
    return result.iterator();
  }
}

final class Selection2<V1, V2, T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>, S extends oak.query.Queryable> implements Many2<T1, T2> {
  private final oak.query.Queryable queryable;
  private final Func<? super V1, ? super V2, ? extends S> flatMap;

  @Contract(pure = true)
  Selection2(oak.query.Queryable queryable, Func<? super V1, ? super V2, ? extends S> flatMap) {
    this.queryable = queryable;
    this.flatMap = flatMap;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var value : queryable) {
      value
        .select(flatMap)
        .peek(struct -> struct.forEach(result::add));
    }
    return result.iterator();
  }
}

final class Peek2<V1, V2> implements Many2<V1, V2> {
  private final oak.query.Queryable queryable;
  private final Con<? super V1, ? super V2> peek;

  @Contract(pure = true)
  Peek2(oak.query.Queryable queryable, Con<? super V1, ? super V2> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public Iterator<Tuple2<V1, V2>> iterator() {
    for (final var value : queryable)
      value.peek(peek);
    return queryable.iterator();
  }
}
