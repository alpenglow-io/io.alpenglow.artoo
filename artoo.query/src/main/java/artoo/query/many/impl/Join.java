package artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$2.Func;
import artoo.func.$2.Pred;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.many.Joining;

import java.util.ArrayList;

import static artoo.type.Nullability.nonNullable;

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
  public final Many<Bag<O, I>> on(final Pred<? super O, ? super I> on) {
    return () -> {
      final var array = new ArrayList<Bag<O, I>>();
      for (final var o : first) {
        for (final var i : second) {
          if (on.test(o, i))
            array.add(new JoiningBag(o, i));
        }
      }
      return array.iterator();
    };
  }

  private final class JoiningBag implements Bag<O, I> {
    private final O left;
    private final I right;

    private JoiningBag(final O left, final I right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public <T> T as(Func<O, I, T> as) {
      return nonNullable(as, "as").apply(left, right);
    }
  }
}
