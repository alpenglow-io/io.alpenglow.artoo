package oak.quill.query;

import oak.quill.Structable;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;

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

  @Override
  public final Tuple2<Structable<O>, Structable<I>> get() {
    return Tuple.of(first, second);
  }
}
