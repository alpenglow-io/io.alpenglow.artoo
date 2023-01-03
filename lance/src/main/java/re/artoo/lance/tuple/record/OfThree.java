package re.artoo.lance.tuple.record;

import re.artoo.lance.tuple.Triple;

public record OfThree<A, B, C>(A first, B second, C third) implements Triple<A, B, C> {
  public enum Empty implements Triple<Object, Object, Object> {
    Default
  }
}
