package oak.collect.query;

import oak.collect.cursor.Cursor;

import java.util.Arrays;
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

  @Override
  public String toString() {
    return String.format("Many{values=%s}", Arrays.toString(values));
  }
}
