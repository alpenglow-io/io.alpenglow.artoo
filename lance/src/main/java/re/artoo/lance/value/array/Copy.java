package re.artoo.lance.value.array;

import re.artoo.lance.value.Array;

import java.util.Iterator;
import java.util.Objects;

import static re.artoo.lance.value.array.Namespace.Companion;

public record Copy<ELEMENT>(ELEMENT[] elements) implements Array<ELEMENT>, Arrangement {
  @SafeVarargs
  public Copy(ELEMENT[] head, ELEMENT... tail) {
    this(Companion.merge(head, tail));
  }
  @SafeVarargs
  public Copy(ELEMENT head, ELEMENT... tail) {
    this(Companion.asArray(head), tail);
  }
  public Copy(ELEMENT[] elements) {
    this.elements = elements;
  }

  @Override
  public Iterator<ELEMENT> iterator() {
    return new Index();
  }

  private final class Index implements Iterator<ELEMENT> {
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
