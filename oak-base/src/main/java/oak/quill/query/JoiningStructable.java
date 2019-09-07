package oak.quill.query;

import oak.collect.Array;
import oak.func.fun.Function2;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.query.JoiningStructable.SelectableJoining;
import oak.quill.single.Single;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface JoiningStructable<O, I> extends Single<Joinable.Joining<Structable<O>, Structable<I>>> {
  default SelectableJoining<O, I> on(final Predicate2<? super O, ? super I> expression) {
    return new On<>(this, nonNullable(expression, "expression"));
  }

  interface SelectableJoining<O, I> extends Structable<Joinable.Joining<O, I>> {
    default <R> Queryable<R> select(final Function2<? super O, ? super I, ? extends R> map) {
      return new JoinSelect<>(this, nonNullable(map, "map"));
    }
  }
}

final class On<O, I> implements SelectableJoining<O, I> {
  private final JoiningStructable<O, I> structable;
  private final Predicate2<? super O, ? super I> expression;

  @Contract(pure = true)
  On(final JoiningStructable<O, I> structable, final Predicate2<? super O, ? super I> expression) {
    this.structable = structable;
    this.expression = expression;
  }

  @NotNull
  @Override
  public final Iterator<Joinable.Joining<O, I>> iterator() {
    final var array = Array.<Joinable.Joining<O, I>>of();
    for (final var joining : structable) {
      for (final var fst : joining.first()) {
        for (final var snd : joining.second()) {
          if (expression.apply(fst, snd)) {
            array.add(new Joinable.Joining<>(fst, snd));
          }
        }
      }
    }
    return array.iterator();
  }
}

final class JoinSelect<O, I, R> implements Queryable<R> {
  private final SelectableJoining<O, I> selectable;
  private final Function2<? super O, ? super I, ? extends R> map;

  @Contract(pure = true)
  JoinSelect(SelectableJoining<O, I> selectable, Function2<? super O, ? super I, ? extends R> map) {
    this.selectable = selectable;
    this.map = map;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<R> iterator() {
    final var array = Array.<R>of();
    for (final var joining : selectable) {
      array.add(map.apply(joining.first(), joining.second()));
    }
    return array.iterator();
  }
}
