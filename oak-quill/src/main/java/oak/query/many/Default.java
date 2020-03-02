package oak.query.many;

import oak.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

enum Default implements Many<Object> {
  Empty;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
