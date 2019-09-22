package oak.quill.query;

import oak.collect.Many;
import oak.func.fun.Function2;
import oak.quill.Structable;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Selectable<O, I> extends Structable<Tuple2<O, I>> {
  default <R> Queryable<R> select(final Function2<? super O, ? super I, ? extends R> map) {
    return new TupleSelect<>(this, nonNullable(map, "map"));
  }
}

final class TupleSelect<O, I, R> implements Queryable<R> {
  private final Selectable<O, I> selectable;
  private final Function2<? super O, ? super I, ? extends R> map;

  @Contract(pure = true)
  TupleSelect(Selectable<O, I> selectable, Function2<? super O, ? super I, ? extends R> map) {
    this.selectable = selectable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<R> iterator() {
    final var array = Many.<R>of();
    for (final var tuple : selectable) {
      for (final R value : tuple.select(map)) {
        array.add(value);
      }
    }
    return array.iterator();
  }
}

