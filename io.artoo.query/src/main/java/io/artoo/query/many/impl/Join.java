package io.artoo.query.many.impl;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.Joining;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public final class Join<O extends Record, I extends Record> implements Joining<O, I> {
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
