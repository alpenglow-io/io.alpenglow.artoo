package artoo.query.many.impl;

import org.jetbrains.annotations.NotNull;
import artoo.query.Many;

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
