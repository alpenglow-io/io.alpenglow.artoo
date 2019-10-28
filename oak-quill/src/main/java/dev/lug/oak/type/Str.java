package dev.lug.oak.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Str {
  @NotNull
  @Contract(value = "_ -> new", pure = true)
  static Str str(final String value) {
    return new StrImpl(value);
  }

  default Str format(Object... objects) {
    return new Format(this, objects);
  }
  default boolean notEquals(String value) { return !this.toString().equals(value); }
}

final class StrImpl implements Str {
  private final String value;

  @Contract(pure = true)
  StrImpl(final String value) {
    this.value = value;
  }

  @Override
  @Contract(pure = true)
  public final String toString() {
    return value;
  }
}

final class Format implements Str {
  private final Str str;
  private final Object[] objects;

  @Contract(pure = true)
  Format(Str str, Object[] objects) {
    this.str = str;
    this.objects = objects;
  }

  @Override
  public final String toString() {
    return String.format(str.toString(), objects);
  }
}
