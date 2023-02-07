package re.artoo.lance.value.array;

import re.artoo.lance.value.Array;

import java.util.Iterator;

import static re.artoo.lance.value.array.Namespace.Companion;

public record Copied<ELEMENT>(ELEMENT[] elements) implements Array<ELEMENT> {
  @SafeVarargs
  public Copied(ELEMENT[] head, ELEMENT... tail) {
    this(Companion.concat(head, tail));
  }
  @SafeVarargs
  public Copied(ELEMENT head, ELEMENT... tail) {
    this(Companion.asArray(head), tail);
  }
  public Copied(ELEMENT[] elements) {
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
