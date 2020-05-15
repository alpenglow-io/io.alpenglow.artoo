package io.artoor.query.many;

import io.artoor.query.Many;
import io.artoor.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BiPredicate;

import static io.artoor.type.Nullability.nonNullable;

public interface Joinable<T1 extends Record> extends Queryable<T1> {
  default <T2 extends Record> Joining<T1, T2> join(final Queryable<T2> second) {
    return new Join<>(this, nonNullable(second, "second"));
  }

  @SuppressWarnings("unchecked")
  default <T2 extends Record> Joining<T1, T2> join(final T2... values) {
    return join(Many.from(values));
  }
}

final class Join<O extends Record, I extends Record> implements Joining<O, I> {
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
  public final Many<Bag<O, I>> on(final BiPredicate<? super O, ? super I> on) {
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
  }
}


