package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public Object tick() {
    return null;
  }

  @Override
  public boolean isTickable() {
    return false;
  }

}
