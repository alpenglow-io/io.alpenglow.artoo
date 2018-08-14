package oak.query.project;

import oak.func.con.Consumer1;
import oak.query.Queryable;

import java.util.Iterator;

final class LookMany<T> implements Queryable<T> {
  private final Queryable<T> some;
  private final Consumer1<T> peek;

  LookMany(final Queryable<T> some, final Consumer1<T> peek) {
    this.some = some;
    this.peek = peek;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Iterator<T> iterator() {
    for (final var value : some) peek.accept(value);
    return some.iterator();
  }
}
