package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public <R> R fetch(TryIntFunction1<? super Object, ? extends R> detach) throws Throwable {
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
