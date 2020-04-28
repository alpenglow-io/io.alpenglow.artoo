package io.artoo.query.many.internal;

import io.artoo.query.Many;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Iteration<T> implements Many<T> {
  private final Iterable<T> iterable;

  public Iteration(Iterable<T> iterable) {this.iterable = iterable;}

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return iterable.iterator();
  }
}
