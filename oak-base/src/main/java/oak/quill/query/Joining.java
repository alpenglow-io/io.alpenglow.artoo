package oak.quill.query;

import oak.collect.Many;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Joining<O, I> extends Structable<Tuple2<O, I>> {
  default Queryable<Tuple2<O, I>> on(final Predicate2<? super O, ? super I> expression) {
    return new On<>(this, nonNullable(expression, "expression"));
  }
}

final class On<O, I> implements Queryable<Tuple2<O, I>> {
  private final Joining<O, I> structable;
  private final Predicate2<? super O, ? super I> expression;

  @Contract(pure = true)
  On(final Joining<O, I> structable, final Predicate2<? super O, ? super I> expression) {
    this.structable = structable;
    this.expression = expression;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<O, I>> iterator() {
    final var array = Many.<Tuple2<O, I>>of();
    for (final var tuple : structable) {
      tuple
        .where(expression)
        .peek((o, i) -> array.add(Tuple.of(o, i)));
    }
    return array.iterator();
  }
}
