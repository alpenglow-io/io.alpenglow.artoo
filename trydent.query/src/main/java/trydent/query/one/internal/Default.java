package trydent.query.one.internal;

import trydent.cursor.Cursor;
import trydent.query.One;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Default implements One<Object> {
  None;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
