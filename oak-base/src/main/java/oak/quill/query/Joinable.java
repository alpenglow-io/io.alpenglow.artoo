package oak.quill.query;

import oak.collect.Many;
import oak.func.con.Consumer2;
import oak.func.fun.Function2;
import oak.quill.Structable;
import oak.quill.single.Nullable;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Joinable<O> extends Structable<O> {
  default <I> Joining<O, I> join(final Structable<I> second) {
    return new Join<>(this, second);
  }
}

final class Join<O, I> implements Joining<O, I> {
  private final Structable<O> first;
  private final Structable<I> second;

  @Contract(pure = true)
  Join(final Structable<O> first, final Structable<I> second) {
    this.first = first;
    this.second = second;
  }

  @NotNull
  @Override
  public Iterator<Tuple2<O, I>> iterator() {
    final var many = Many.<Tuple2<O, I>>of();
    for (final var o : first) {
      for (final var i : second) {
        many.add(Tuple.of(o, i));
      }
    }
    return many.iterator();
  }
}
