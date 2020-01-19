package dev.lug.oak.query.tuple;

import dev.lug.oak.func.con.Consumer2;
import dev.lug.oak.func.fun.Function2;
import dev.lug.oak.query.Q.Just2AsManyTuple2;
import dev.lug.oak.query.Q.Just2AsTuple2;
import dev.lug.oak.query.Queryable;
import dev.lug.oak.query.many.Projectable;
import dev.lug.oak.query.many.Many;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public interface Projectable2<V1, V2> extends Projectable<Tuple2<V1, V2>> {
  default <R> Many<R> select(final dev.lug.oak.query.Q.AsJust2<? super V1, ? super V2, ? extends R> map) {
    return new SelectT2<>(this, Nullability.nonNullable(map, "map"));
  }

  default <T1, T2, T extends Tuple2<T1, T2>> Queryable2<T1, T2> select(final Just2AsTuple2<? super V1, ? super V2, ? extends T> map) {
    return new Select2<>(this, Nullability.nonNullable(map, "map"));
  }

  default <T1, T2, T extends Tuple2<T1, T2>, Q extends Queryable<T>> Queryable2<T1, T2> select(final Just2AsManyTuple2<? super V1, ? super V2, ? extends Q> flatMap) {
    return new Selection2<>(this, Nullability.nonNullable(flatMap, "flatMap"));
  }

  default Queryable2<V1, V2> peek2(final Consumer2<? super V1, ? super V2> peek) {
    return new Peek2<>(this, Nullability.nonNullable(peek, "peek"));
  }
}

final class SelectT2<V1, V2, R> implements Many<R> {
  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Function2<? super V1, ? super V2, ? extends R> map;

  @Contract(pure = true)
  SelectT2(final Queryable<Tuple2<V1, V2>> queryable, final Function2<? super V1, ? super V2, ? extends R> map) {
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

final class Select2<V1, V2, T1, T2, T extends Tuple2<T1, T2>> implements Queryable2<T1, T2> {
  private final Queryable<Tuple2<V1, V2>> queryable;
  private final Function2<? super V1, ? super V2, ? extends T> map;

  @Contract(pure = true)
  Select2(final Queryable<Tuple2<V1, V2>> queryable, final Function2<? super V1, ? super V2, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var tuple : queryable) {
      tuple
        .select(map)
        .peek(result::add);
    }
    return result.iterator();
  }
}

final class Selection2<V1, V2, T1, T2, T extends Tuple2<T1, T2>, S extends Queryable<T>> implements Queryable2<T1, T2> {
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

final class Peek2<V1, V2> implements Queryable2<V1, V2> {
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
