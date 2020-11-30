package io.artoo.lance.type;

import io.artoo.lance.func.Suppl;

public abstract class Inline<T> implements Suppl.Uni<T> {
  private final T value;

  protected Inline(final T value) {this.value = value;}

  @Override
  public final T tryGet() throws Throwable {
    return value;
  }
}
