package oak.query.one.internal;

import oak.func.$2.IntCons;
import oak.query.Queryable;
import oak.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Sneakable<T> extends One<T> {
  default IntCons<? super T> sneak() {
    return IntCons.nothing();
  }

  final class Sneak<T> implements Sneakable<T> {
    private final Queryable<T> queryable;
    private final IntCons<? super T> peek;

    @Contract(pure = true)
    public Sneak(Queryable<T> queryable, IntCons<? super T> peek) {
      this.queryable = queryable;
      this.peek = peek;
    }

    @Contract(pure = true)
    @Override
    public final IntCons<? super T> sneak() {
      return this.peek;
    }

    @NotNull
    @Override
    public final Iterator<T> iterator() {
      return queryable.iterator();
    }
  }
}
