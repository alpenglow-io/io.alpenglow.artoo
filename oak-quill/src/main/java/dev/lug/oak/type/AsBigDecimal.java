package dev.lug.oak.type;

import java.math.BigDecimal;

@FunctionalInterface
public interface AsBigDecimal {
  BigDecimal eval();
}
