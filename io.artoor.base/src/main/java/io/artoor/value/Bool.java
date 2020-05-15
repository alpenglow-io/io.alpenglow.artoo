package io.artoor.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Bool(boolean eval) {
  public static final Bool True = new Bool(true);
  public static final Bool False = new Bool(false);

  @Contract(" -> new")
  public final @NotNull Bool not() {
    return new Bool(!eval);
  }
}
