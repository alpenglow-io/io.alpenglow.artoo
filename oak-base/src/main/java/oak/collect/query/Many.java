package oak.collect.query;

import oak.collect.cursor.Cursor;

import java.util.Iterator;

final class Many<T> implements Queryable<T> {
  private final T[] values;

  Many(final T[] values) {
    this.values = values;
  }

  @Override
  public final Iterator<T> iterator() {
    return Cursor.forward(values);
  }
}
