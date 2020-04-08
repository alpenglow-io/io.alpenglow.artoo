package trydent.desktop.component.grid;

import java.util.function.Supplier;

@FunctionalInterface
public interface Percent extends Supplier<Double> {
  static Percent percent(double value) {
    return () -> value;
  }
}
