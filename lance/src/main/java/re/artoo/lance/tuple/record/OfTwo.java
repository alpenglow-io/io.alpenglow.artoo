package re.artoo.lance.tuple.record;

import re.artoo.lance.tuple.Pair;

public record OfTwo<A, B>(A first, B second) implements Pair<A, B> {
  public enum Empty implements Pair<Object, Object> {
    Default
  }
}
