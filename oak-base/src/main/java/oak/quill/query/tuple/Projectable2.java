package oak.quill.query.tuple;

import oak.collect.Many;
import oak.func.fun.Function2;
import oak.quill.Structable;
import oak.quill.query.Projectable;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Projectable2<V1, V2> extends Projectable<Tuple2<V1, V2>> {
  default <T1, T2, T extends Tuple2<T1, T2>> Queryable2<T1, T2> select2(final Function2<? super V1, ? super V2, ? extends T> map) {
    return new Select2<>(this, nonNullable(map, "map"));
  }

  default <T1, T2, T extends Tuple2<T1, T2>, S extends Structable<T>> Queryable2<T1, T2> selection2(final Function2<? super V1, ? super V2, ? extends S> flatMap) {
    return new Selection2<>(this, nonNullable(flatMap, "flatMap"));
  }
}

final class Select2<V1, V2, T1, T2, T extends Tuple2<T1, T2>> implements Queryable2<T1, T2> {
  private final Structable<Tuple2<V1, V2>> structable;
  private final Function2<? super V1, ? super V2, ? extends T> map;

  @Contract(pure = true)
  Select2(final Structable<Tuple2<V1, V2>> structable, final Function2<? super V1, ? super V2, ? extends T> map) {
    this.structable = structable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = Many.<Tuple2<T1, T2>>of();
    for (final var tuple : structable) {
      tuple
        .select(map)
        .peek(result::add);
    }
    return result.iterator();
  }
}

final class Selection2<V1, V2, T1, T2, T extends Tuple2<T1, T2>, S extends Structable<T>> implements Queryable2<T1, T2> {
  private final Structable<Tuple2<V1, V2>> structable;
  private final Function2<? super V1, ? super V2, ? extends S> flatMap;

  @Contract(pure = true)
  Selection2(Structable<Tuple2<V1, V2>> structable, Function2<? super V1, ? super V2, ? extends S> flatMap) {
    this.structable = structable;
    this.flatMap = flatMap;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<T1, T2>> iterator() {
    final var result = Many.<Tuple2<T1, T2>>of();
    for (final var value : structable) {
      value
        .select(flatMap)
        .peek(struct -> struct.forEach(result::add));
    }
    return result.iterator();
  }
}
