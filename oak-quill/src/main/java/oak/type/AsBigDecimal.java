package oak.type;

import java.math.BigDecimal;

@FunctionalInterface
public interface AsBigDecimal {
  BigDecimal eval();
}
