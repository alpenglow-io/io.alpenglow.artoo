package oak.query.many.internal;

import oak.cursor.$2.Cursor;
import oak.func.$2.Pred;
import oak.query.$2.many.Many;
import oak.query.Queryable;
import oak.query.many.Joining;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class Join<O, I> implements Joining<O, I> {
  private final Queryable<O> first;
  private final Queryable<I> second;

  @Contract(pure = true)
  public Join(final Queryable<O> first, final Queryable<I> second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Many<O, I> on(final Pred<? super O, ? super I> on) {
    return () -> {
      final var array = new ArrayList<Union<O, I>>();
      for (final var o : first) {
        for (final var i : second) {
          if (on.test(o, i))
            array.add(Union.of(o, i));
        }
      }
      return Cursor.from(array.iterator());
    };
  }
}
