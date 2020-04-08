package trydent.query.many;

import trydent.cursor.Cursor;
import trydent.query.Many;
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
