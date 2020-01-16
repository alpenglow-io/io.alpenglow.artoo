package dev.lug.oak.query.many.tuple;

import dev.lug.oak.func.con.Consumer3;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.many.Projectable;
import dev.lug.oak.query.many.Queryable;
import dev.lug.oak.query.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;

public interface Projectable3<V1, V2, V3> extends Projectable<Tuple3<V1, V2, V3>> {
  default <R> Queryable<R> select(final Function3<? super V1, ? super V2, ? super V3, ? extends R> map) {
    return new SelectT3<>(this, nonNullable(map, "map"));
  }

  default <T1, T2, T3, T extends Tuple3<T1, T2, T3>> Queryable3<T1, T2, T3> select3(final Function3<? super V1, ? super V2, ? super V3, ? extends T> map) {
    return new Select3<>(this, nonNullable(map, "map"));
  }

  default <T1, T2, T3, T extends Tuple3<T1, T2, T3>, S extends Structable<T>> Queryable3<T1, T2, T3> selection3(final Function3<? super V1, ? super V2, ? super V3, ? extends S> flatMap) {
    return new Selection3<>(this, nonNullable(flatMap, "flatMap"));
  }

  default Queryable3<V1, V2, V3> peek2(final Consumer3<? super V1, ? super V2, ? super V3> peek) {
    return new Peek3<>(this, nonNullable(peek, "peek"));
  }
}

final class SelectT3<V1, V2, V3, R> implements Queryable<R> {
  private final Structable<Tuple3<V1, V2, V3>> structable;
  private final Function3<? super V1, ? super V2, ? super V3, ? extends R> map;

  @Contract(pure = true)
  SelectT3(final Structable<Tuple3<V1, V2, V3>> structable, final Function3<? super V1, ? super V2, ? super V3, ? extends R> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var result = new ArrayList<R>();
    for (final var tuple3 : structable) {
      tuple3
        .select(map)
        .forEach(result::add);
    }
    return result.iterator();
  }
}

final class Select3<V1, V2, V3, T1, T2, T3, T extends Tuple3<T1, T2, T3>> implements Queryable3<T1, T2, T3> {
  private final Structable<Tuple3<V1, V2, V3>> structable;
  private final Function3<? super V1, ? super V2, ? super V3, ? extends T> map;

  @Contract(pure = true)
  Select3(final Structable<Tuple3<V1, V2, V3>> structable, final Function3<? super V1, ? super V2, ? super V3, ? extends T> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = new ArrayList<Tuple3<T1, T2, T3>>();
    for (final var tuple3 : structable) {
      tuple3
        .select(map)
        .peek(result::add);
    }
    return result.iterator();
  }
}

final class Selection3<V1, V2, V3, T1, T2, T3, T extends Tuple3<T1, T2, T3>, S extends Structable<T>> implements Queryable3<T1, T2, T3> {
  private final Structable<Tuple3<V1, V2, V3>> structable;
  private final Function3<? super V1, ? super V2, ? super V3, ? extends S> flatMap;

  @Contract(pure = true)
  Selection3(Structable<Tuple3<V1, V2, V3>> structable, Function3<? super V1, ? super V2, ? super V3, ? extends S> flatMap) {
    this.structable = structable;
    this.flatMap = flatMap;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, T3>> iterator() {
    final var result = new ArrayList<Tuple3<T1, T2, T3>>();
    for (final var tuple3 : structable) {
      tuple3
        .select(flatMap)
        .peek(struct -> struct.forEach(result::add));
    }
    return result.iterator();
  }
}

final class Peek3<V1, V2, V3> implements Queryable3<V1, V2, V3> {
  private final Structable<Tuple3<V1, V2, V3>> structable;
  private final Consumer3<? super V1, ? super V2, ? super V3> peek;

  @Contract(pure = true)
  Peek3(Structable<Tuple3<V1, V2, V3>> structable, Consumer3<? super V1, ? super V2, ? super V3> peek) {
    this.structable = structable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public Iterator<Tuple3<V1, V2, V3>> iterator() {
    for (final var tuple3 : structable)
      tuple3.peek(peek);
    return structable.iterator();
  }
}
