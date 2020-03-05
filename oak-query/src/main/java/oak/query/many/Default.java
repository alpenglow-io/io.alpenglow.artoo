package oak.query.many;

import oak.cursor.Cursor;
import oak.query.Many;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Default implements Many<Object> {
  None;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
