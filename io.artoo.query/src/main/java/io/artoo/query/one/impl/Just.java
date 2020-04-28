package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;
import io.artoo.query.One;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Just<T> implements One<T> {
  private final T value;

  public Just(T value) {this.value = value;}

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.of(value);
  }
}
