package io.artoo.fxcalibur.component.grid;

import java.util.function.Supplier;

@FunctionalInterface
public interface Pixel extends Supplier<Integer> {
  static Pixel pixel(int value) {
    return () -> value;
  }
}
