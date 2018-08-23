package oak.collect.query.element;

import oak.collect.cursor.Cursor;
import oak.collect.query.Queryable;

import java.util.Iterator;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

final class First<S> implements Element<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> some;

  First(Queryable<S> some) {
    this.some = some;
  }

  @Override
  public final Iterator<S> iterator() {
    return Cursor.maybe(some.iterator().next());
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }
}
