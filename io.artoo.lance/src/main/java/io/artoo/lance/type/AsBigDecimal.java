package io.artoo.lance.type;

import java.math.BigDecimal;

@FunctionalInterface
public interface AsBigDecimal {
  BigDecimal eval();
}
