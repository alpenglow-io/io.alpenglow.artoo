package io.artoor.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;

public interface Str {
  String Empty = "";

  @NotNull
  @Contract(value = "_ -> new", pure = true)
  static Str $(final String value) {
    return new StrImpl(value);
  }

  default Str format(Object... objects) {
    return new Format(this, new Formatter(), objects);
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
  private final Formatter formatter;
  private final Object[] objects;

  @Contract(pure = true)
  Format(final Str str, final Formatter formatter, final Object... objects) {
    this.str = str;
    this.formatter = formatter;
    this.objects = objects;
  }

  @Override
  public final String toString() {
    return formatter.format(str.toString(), objects).toString();
  }
}
