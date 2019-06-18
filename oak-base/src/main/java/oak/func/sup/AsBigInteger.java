package oak.func.sup;

import oak.func.Functional;

import java.math.BigInteger;

@FunctionalInterface
public interface AsBigInteger extends Functional.Sup {
  BigInteger get();
}
