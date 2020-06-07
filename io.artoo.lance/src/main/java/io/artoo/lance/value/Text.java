package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Text(String eval) {
  public Text { assert eval != null; }

  public static final Text Empty = new Text("");

  @Contract(value = "_ -> new", pure = true)
  public static @NotNull Text let(final String value) {
    return new Text(value);
  }

  public static Text format(final String pattern, final Object... params) {
    return new Text(String.format(pattern, params));
  }

  @Contract(" -> new")
  public @NotNull Text upperCased() { return new Text(eval.toUpperCase()); }

  @Contract(" -> new")
  public @NotNull Text lowerCased() { return new Text(eval.toLowerCase()); }

  public int length() { return eval.length(); }

  public boolean is(final String value) {
    return eval.equals(value);
  }
}
