package re.artoo.lance.value.array;

import re.artoo.lance.value.Array;

import java.util.Objects;

import static re.artoo.lance.value.array.Arrayable.merge;

public final class Some<ELEMENT> implements Array<ELEMENT> {
  private final ELEMENT[] elements;
  private int index;
  public Some(ELEMENT[] elements) {
    this(elements, 0);
  }
  private Some(ELEMENT[] elements, int index) {
    this.elements = elements;
    this.index = index;
  }

  @Override
  public boolean hasNext() {
    return index < elements.length;
  }

  @Override
  public ELEMENT next() {
    return elements[index++];
  }

  @SafeVarargs
  @Override
  public final Array<ELEMENT> concat(ELEMENT... elements) {
    return new Some<>(merge(this.elements, elements));
  }

  @Override
  public Array<ELEMENT> concat(Array<ELEMENT> array) {
    return switch (array) {
      case Some<ELEMENT> some -> concat(some.elements);
      case Empty ignored, null -> this;
    };
  }

  @Override
  public boolean equals(Object obj) {
    return switch (obj) {
      case Some<?> some -> Objects.deepEquals(this.elements, some.elements);
      case null, default -> false;
    };
  }

  @Override
  public String toString() {
    var strings = new StringBuilder("Array [");
    for (int index = 0; index < elements.length; index++) {
      if (index == 0) {
        strings.append(elements[index]);
      } else {
        strings.append(", %s".formatted(elements[index]));
      }
    }
    strings.append("]");
    return strings.toString();
  }
}
