package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.Con;
import oak.func.Func;
import oak.func.fun.IntFunction2;
import oak.query.Queryable;
import oak.query.many.$2.Many;
import oak.query.$3.Queryable3;
import dev.lug.oak.query.tuple3.Tuple3;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.*;

public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectIth<T, R> extends IntFunc<T, R> {}
  interface SelectMany<T, R, M extends oak.query.Many<R>> extends Func<T, M> {}
  interface SelectUnion2<T, R1, R2, U extends Union<R1, R2>> extends oak.func.Func<T, U> {}
  interface SelectUnion3<T, R1, R2, R3, U extends oak.union.$3.Union<R1, R2, R3>> extends oak.func.Func<T, U> {}

  default <R> oak.query.Many<R> select(final Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select((index, value) -> select.apply(value));
  }

  default <R> oak.query.Many<R> select(final SelectIth<? super T, ? extends R> selectIth) {
    nonNullable(selectIth, "selectIth");
    return () -> {
      final var array = new ArrayList<R>();
      var index = 0;
      for (var cursor = this.iterator(); cursor.hasNext(); index++) {
        array.add(selectIth.applyInt(index, cursor.next()));
      }
      return array.iterator();
    };
  }

  default <R1, R2, U extends Union<R1, R2>> Many<R1, R2> select(final SelectUnion2<? super T, ? extends R1, ? extends R2, ? extends U> selectUnion2) {
    nonNullable(selectUnion2, "selectUnion2");
    return new SelectTuple<>(this, nonNullable(tuple, "tuple"));
  }

  default <T1, T2, T3, U extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select(final JustAsTuple3<? super T, ? extends U> tuple) {
    return new SelectTuple3<>(this, nonNullable(tuple, "tuple"));
  }

  default <R, S extends Queryable<R>> oak.query.Many select(final AnyAsQueryable<? super T, ? super R, ? extends S> flatMap) {
    return new Selection<>(new Select<>(this, nonNullable(flatMap, "flatMap")));
  }

  @Deprecated(forRemoval = true)
  default oak.query.Many peek(final Con<? super T> peek) {
    return new Peek<>(this, nonNullable(peek, "peek"));
  }

}

final class Selection<R, S extends Queryable<R>> implements oak.query.Many {
  private final Queryable<S> structables;

  @Contract(pure = true)
  Selection(Queryable<S> structables) {
    this.structables = structables;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    for (final var structable : structables) {
      for (final var value : structable) {
        array.add(value);
      }
    }
    return array.iterator();
  }
}

final class Peek<T> implements oak.query.Many {
  private final Queryable<T> queryable;
  private final Con<? super T> peek;

  @Contract(pure = true)
  Peek(final Queryable<T> queryable, final Con<? super T> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    for (final var value : queryable) peek.accept(value);
    return queryable.iterator();
  }
}

final class SelectIth<S, R> implements oak.query.Many {
  private final Queryable<S> queryable;
  private final IntFunction2<? super S, ? extends R> mapIndex;

  @Contract(pure = true)
  SelectIth(final Queryable<S> queryable, final IntFunction2<? super S, ? extends R> mapIndex) {
    this.queryable = queryable;
    this.mapIndex = mapIndex;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {

  }
}

final class SelectTuple<V, T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> implements Many<T1, T2> {
  private final Queryable<V> queryable;
  private final Func<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple(final Queryable<V> queryable, final Func<? super V, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = new ArrayList<Tuple2<T1, T2>>();
    for (final var value : queryable) result.add(map.apply(value));
    return result.iterator();
  }
}

final class SelectTuple3<V, T1, T2, T3, T extends Tuple3<T1, T2, T3>> implements Queryable3<T1, T2, T3> {
  private final Queryable<V> queryable;
  private final Func<? super V, ? extends T> map;

  @Contract(pure = true)
  SelectTuple3(final Queryable<V> queryable, final Func<? super V, ? extends T> map) {
    this.queryable = queryable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = new ArrayList<Tuple3<T1, T2, T3>>();
    for (final var value : queryable) result.add(map.apply(value));
    return result.iterator();
  }
}
