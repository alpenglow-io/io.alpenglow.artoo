package re.artoo.lance.tuple.record;

import re.artoo.lance.tuple.Quintuple;

public record OfFive<A, B, C, D, E>(A first, B second, C third, D forth, E fifth) implements Quintuple<A, B, C, D, E> {
  public enum Empty implements Quintuple<Object, Object, Object, Object, Object> {
    Default
  }
}
