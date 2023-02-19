package re.artoo.lance.value.array;

import re.artoo.lance.value.Array;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;

public record Lone<ELEMENT>(ELEMENT element) implements Array<ELEMENT> {
  public Lone { requireNonNull(element, "Can't initialize sequence, element is null"); }
  @Override
  public Iterator<ELEMENT> iterator() {
    return new One();
  }

  public ELEMENT[] elements() { return Elements.Companion.asArray(element); }

  private final class One implements Iterator<ELEMENT> {
    private boolean read = false;

    @Override
    public boolean hasNext() {
      return !read;
    }

    @Override
    public ELEMENT next() {
      try {
        return !read ? element : null;
      } finally {
        read = true;
      }
    }
  }
}
