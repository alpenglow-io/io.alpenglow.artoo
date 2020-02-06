package dev.lug.oak.query.many2;

import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.query.Many;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.Queryable.Tuple2AsAny;
import dev.lug.oak.query.Queryable.Tuple2AsQueryable;
import dev.lug.oak.query.Queryable.Tuple2AsTuple;
import dev.lug.oak.query.Queryable2;
import dev.lug.oak.query.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.lug.oak.query.Queryable.P.as;
import static dev.lug.oak.type.Nullability.nonNullable;

public interface Projectable2<V1, V2> extends Queryable2<V1, V2> {
  default <R> Many<R> select(final Tuple2AsAny<? super V1, ? super V2, ? extends R> map) {
    return new Select2AsAny<>(this, nonNullable(map, "map"));
  }

  default <T1, T2, T extends Tuple2<T1, T2>> Many2<T1, T2> select(final Tuple2AsTuple<? super V1, ? super V2, ? extends T> map) {
    return new Select2As2<>(this, nonNullable(map, "map"));
  }

  default <T1, T2, T extends Tuple2<T1, T2>, Q extends Queryable<T>> Many2<T1, T2> select(final Tuple2AsQueryable<? super V1, ? super V2, ? extends T, ? extends Q> flatMap) {
    return new Selection2<>(this, nonNullable(flatMap, "flatMap"));
  }

  default Many2<V1, V2> peek2(final Consumer2<? super V1, ? super V2> peek) {
    return new Peek2<>(this, nonNullable(peek, "peek"));
  }
}

final class Select2AsAny<V1, V2, R> implements Many<R> {
  private final Queryable2<V1, V2> queryable;
  private final Function2<? super V1, ? super V2, ? extends R> map;

  @Contract(pure = true)
  Select2AsAny(final Queryable2<V1, V2> queryable, final Function2<? super V1, ? super V2, ? extends R> map) {
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

final class Select2As2<V1, V2, T1, T2, T extends Tuple2<T1, T2>> implements Many2<T1, T2> {
  private final Queryable2<V1, V2> queryable;
  private final Function2<? super V1, ? super V2, ? extends T> map;

  @Contract(pure = true)
  Select2As2(final Queryable2<V1, V2> queryable, final Function2<? super V1, ? super V2, ? extends T> map) {
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

final class Selection2<V1, V2, T1, T2, T extends Tuple2<T1, T2>, S extends Queryable<T>> implements Many2<T1, T2> {
  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Function2<? super V1, ? super V2, ? extends S> flatMap;

  @Contract(pure = true)
  Selection2(Queryable<Tuple2<V1, V2>> queryable, Function2<? super V1, ? super V2, ? extends S> flatMap) {
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
  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Consumer2<? super V1, ? super V2> peek;

  @Contract(pure = true)
  Peek2(Queryable<Tuple2<V1, V2>> queryable, Consumer2<? super V1, ? super V2> peek) {
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
