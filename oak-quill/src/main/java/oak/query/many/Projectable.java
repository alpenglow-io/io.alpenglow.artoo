package oak.query.many;

import oak.func.$2.IntFunc;
import oak.func.Cons;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.$3.Queryable3;
import dev.lug.oak.query.tuple3.Tuple3;
import oak.query.many.$3.Many;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.type.Nullability.*;

@SuppressWarnings("unchecked")
public interface Projectable<T> extends Queryable<T> {
  interface Select<T, R> extends Func<T, R> {}
  interface SelectIth<T, R> extends IntFunc<T, R> {}
  interface SelectMany<T, R, M extends oak.query.Many<R>> extends Func<T, M> {}
  interface SelectIthMany<T, R, M extends oak.query.Many<R>> extends IntFunc<T, M> {}
  interface SelectUnion2<T, R1, R2> extends oak.func.Func<T, Union<R1, R2>> {}
  interface SelectUnion3<T, R1, R2, R3> extends oak.func.Func<T, oak.union.$3.Union<R1, R2, R3>> {}

  static <T, R> SelectIth<T, R> ith(final SelectIth<T, R> selectIth) {
    return selectIth;
  }

  static <T, R> SelectIthMany<T, R> ithMany(final SelectIthMany<T, R> selectIthMany) {
    return selectIthMany;
  }

  default <R> oak.query.Many<R> select(final Select<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select(ith((index, value) -> select.apply(value)));
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

  @SuppressWarnings("unchecked")
  default <R1, R2> oak.query.many.$2.Many<R1, R2> select(final SelectUnion2<? super T, ? extends R1, ? extends R2> select) {
    nonNullable(select, "select");
    return () -> {
      final var result = new ArrayList<Union<R1, R2>>();
      for (final var it : this) result.add((Union<R1, R2>) select.apply(it));
      return result.iterator();
    };
  }

  default <R1, R2, R3> Many<R1, R2, R3> select(final SelectUnion3<? super T, ? extends R1, ? extends R2, ? extends R3> select) {
    nonNullable(select, "select");
    return () -> {
      final var result = new ArrayList<oak.union.$3.Union<R1, R2, R3>>();
      for (final var it : this) result.add((oak.union.$3.Union<R1, R2, R3>) select.apply(it));
      return result.iterator();
    };
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(final SelectMany<? super T, ? extends R, M> selectMany) {
    nonNullable(selectMany, "selectMany");
    return () -> select()
  }

  default <R, M extends oak.query.Many<R>> oak.query.Many<R> select(final SelectIthMany<? super T, ? super R, M> selectIthMany) {
    nonNullable(selectIthMany, "selectIthMany");
    return () -> select(ith((index, value) -> selectIthMany.applyInt(index, value))).iterator();
  }

  @Deprecated(forRemoval = true)
  default oak.query.Many peek(final Cons<? super T> peek) {
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
  private final Cons<? super T> peek;

  @Contract(pure = true)
  Peek(final Queryable<T> queryable, final Cons<? super T> peek) {
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

final class SelectTuple<V, T1, T2, T extends Projectable2<V1, V2> & Filterable2<V1, V2> & Peekable2<V1, V2>> implements oak.query.many.$2.Many {
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
