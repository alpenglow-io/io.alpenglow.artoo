package re.artoo.lance.experimental.array;

import re.artoo.lance.value.Array;

import java.util.Arrays;
import java.util.Iterator;

import static re.artoo.lance.experimental.array.Elements.Companion;

public record Some<ELEMENT>(ELEMENT[] elements) implements Array<ELEMENT> {
  @SafeVarargs
  public Some(ELEMENT[] head, ELEMENT... tail) {
    this(Companion.concat(head, tail));
  }

  @SafeVarargs
  public Some(ELEMENT head, ELEMENT... tail) {
    this(Companion.asArray(head), tail);
  }

  @SafeVarargs
  public Some(ELEMENT[] head, ELEMENT body, ELEMENT... tail) {
    this(Companion.concat(head, Companion.concat(Companion.asArray(body), tail)));
  }

  public Some(ELEMENT[] head, ELEMENT tail) {
    this(head, Companion.asArray(tail));
  }

  @Override
  public String toString() {
    return Arrays.toString(elements);
  }

  @Override
  public Iterator<ELEMENT> iterator() {
    return new N();
  }

  private final class N implements Iterator<ELEMENT> {
    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < elements.length;
    }

    @Override
    public ELEMENT next() {
      return elements[index++];
    }
  }
}
