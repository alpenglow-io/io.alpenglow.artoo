package re.artoo.lance.tuple.record;

import re.artoo.lance.tuple.Quadruple;

public record OfFour<A, B, C, D>(A first, B second, C third, D forth) implements Quadruple<A, B, C, D> {
  public enum Empty implements Quadruple<Object, Object, Object, Object> {
    Default
  }
}
