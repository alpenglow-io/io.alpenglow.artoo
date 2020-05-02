package io.artoo.query.many;

import io.artoo.cursor.Cursor;
import io.artoo.query.Many;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Default implements Many<Record> {
  None;

  @NotNull
  @Override
  public Iterator<Record> iterator() {
    return Cursor.none();
  }
}
