package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public Object fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public <R> R as(final Routine<Object, R> routine) {
    return routine.onSelf().apply(null);
  }
}
