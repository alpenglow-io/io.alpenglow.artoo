package oak.quill.query;

import oak.collect.Array;
import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.query.Joinable.Joining;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Nullability.nonNullable;

public interface Joinable<O> extends Structable<O> {
  default <I, K, R> Queryable<R> join(
    final Structable<I> inner,
    final Function1<? super O, ? extends K> outerKey,
    final Function1<? super I, ? extends K> innerKey,
    final Function2<? super O, ? super I, ? extends R> result
  ) {
    return new Join<>(
      this,
      nonNullable(inner, "inner"),
      nonNullable(outerKey, "outerKey"),
      nonNullable(innerKey, "innerKey"),
      nonNullable(result, "result")
    );
  }

  default <I> JoiningStructable<O, I> join(final Structable<I> second) {
    return new JoinStruct<>(this, second);
  }

  final class Joining<O, I> {
    private final O first;
    private final I second;

    @Contract(pure = true)
    Joining(O first, I second) {
      this.first = first;
      this.second = second;
    }

    @Contract(pure = true)
    public O first() { return first; }
    @Contract(pure = true)
    public I second() { return second; }
  }
}



final class Join<O, I, K, R> implements Queryable<R> {
  private final Structable<O> outer;
  private final Structable<I> inner;
  private final Function1<? super O, ? extends K> outerKey;
  private final Function1<? super I, ? extends K> innerKey;
  private final Function2<? super O, ? super I, ? extends R> result;

  @Contract(pure = true)
  Join(
    final Structable<O> outer,
    final Structable<I> inner,
    final Function1<? super O, ? extends K> outerKey,
    final Function1<? super I, ? extends K> innerKey,
    final Function2<? super O, ? super I, ? extends R> result
  ) {
    this.outer = outer;
    this.inner = inner;
    this.outerKey = outerKey;
    this.innerKey = innerKey;
    this.result = result;
  }

  @NotNull
  @Override
  @Contract(pure = true)
  public final Iterator<R> iterator() {
    final var joins = Array.<Joining<O, I>>of();
    for (final var out : this.outer) {
      final var oKey = outerKey.apply(out);
      for (final var in : this.inner) {
        final var iKey = innerKey.apply(in);
        if (oKey.equals(iKey)) {
          joins.add(new Joining<>(out, in));
        }
      }
    }
    final var results = Array.<R>of();
    for (final var join : joins) {
      results.add(result.apply(join.first(), join.second()));
    }
    return results.iterator();
  }
}

final class JoinStruct<O, I> implements JoiningStructable<O, I> {
  private final Structable<O> first;
  private final Structable<I> second;

  @Contract(pure = true)
  JoinStruct(final Structable<O> first, final Structable<I> second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public Iterator<Joining<Structable<O>, Structable<I>>> iterator() {
    return Cursor.of(new Joining<>(first, second));
  }
}

