package io.artoor.type.composite;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static io.artoor.type.Nullability.nonNullable;

public final class Composite<R extends Record> {
  private final R record;

  @Contract(pure = true)
  public Composite(final R record) {this.record = record;}

  public final <T> T as(final @NotNull Function<? super R, ? extends T> as) {
    return nonNullable(nonNullable(as, "as").apply(record), "returnValue");
  }
}
