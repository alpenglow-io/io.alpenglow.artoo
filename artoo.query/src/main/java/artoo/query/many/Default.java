package artoo.query.many;

import org.jetbrains.annotations.NotNull;
import artoo.cursor.Cursor;
import artoo.query.Many;

import java.util.Iterator;

public enum Default implements Many<Object> {
  None;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
