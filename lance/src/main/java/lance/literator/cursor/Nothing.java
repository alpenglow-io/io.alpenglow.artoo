package lance.literator.cursor;

import lance.literator.Cursor;
import lance.literator.cursor.routine.Routine;

public enum Nothing implements Cursor<Object> {
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
