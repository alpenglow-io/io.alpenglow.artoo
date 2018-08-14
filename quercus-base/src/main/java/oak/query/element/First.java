package oak.query.element;

import oak.query.Maybe;
import oak.query.Queryable;

import java.util.Iterator;

final class First<S> implements Element<S, Queryable<S>>, Queryable<S> {
  private final Maybe<S> element;

  First(Maybe<S> element) {
    this.element = element;
  }

  @Override
  public final Iterator<S> iterator() {
    return element.iterator();
  }
}
