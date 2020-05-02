package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;
import io.artoo.query.One;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Default implements One<Record> {
  None;

  @NotNull
  @Override
  public Iterator<Record> iterator() {
    return Cursor.none();
  }
}
