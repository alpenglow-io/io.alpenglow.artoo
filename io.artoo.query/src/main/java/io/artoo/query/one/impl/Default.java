package io.artoo.query.one.impl;

import org.jetbrains.annotations.NotNull;
import io.artoo.cursor.Cursor;
import io.artoo.query.One;

import java.util.Iterator;

public enum Default implements One<Object> {
  None;

  @NotNull
  @Override
  public Iterator<Object> iterator() {
    return Cursor.none();
  }
}
