package oak.query.one;

import oak.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

enum Default implements One<Object> {
  None;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
