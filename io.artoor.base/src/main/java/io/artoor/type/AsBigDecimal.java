package io.artoor.type;

import java.math.BigDecimal;

@FunctionalInterface
public interface AsBigDecimal {
  BigDecimal eval();
}
