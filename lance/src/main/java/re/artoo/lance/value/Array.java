package re.artoo.lance.value;

import re.artoo.lance.value.array.Arrangement;
import re.artoo.lance.value.array.Empty;
import re.artoo.lance.value.array.Copy;

import java.util.RandomAccess;

public sealed interface Array<ELEMENT> extends Iterable<ELEMENT>, RandomAccess, Arrangement permits Empty, Copy {
  @SafeVarargs
  static <ELEMENT> Array<ELEMENT> of(ELEMENT... elements) {
    return new Copy<>(elements);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> of(ELEMENT element, ELEMENT... elements) {
    return element == null ? empty() : new Copy<>(element, elements);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> empty() {
    return (Array<ELEMENT>) Empty.Default;
  }

  @SuppressWarnings("unchecked")
  default Array<ELEMENT> concat(ELEMENT... elements) {
    return Array.of(elements);
  }
  default Array<ELEMENT> concat(Array<ELEMENT> array) {
    return switch (this) {
      case Copy<ELEMENT> head when array instanceof Copy<ELEMENT> tail -> new Copy<>(head.elements(), tail.elements());
      case Empty head when array instanceof Copy<ELEMENT> tail -> tail;
      default -> this;
    };
  }
  default Array<ELEMENT> push(ELEMENT element) {
    return Array.of(element);
  }
  default ELEMENT at(int index) {
    throw new ArrayIndexOutOfBoundsException(index);
  }
}


