package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public <R> R scroll(TryIntFunction1<? super Object, ? extends R> fetch) throws Throwable {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

}
