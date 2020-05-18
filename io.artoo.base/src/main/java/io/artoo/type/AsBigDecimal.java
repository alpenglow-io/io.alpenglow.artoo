package io.artoo.type;

import java.math.BigDecimal;

@FunctionalInterface
public interface AsBigDecimal {
  BigDecimal eval();
}
