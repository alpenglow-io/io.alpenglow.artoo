package re.artoo.lance.value;

import re.artoo.lance.value.array.Empty;
import re.artoo.lance.value.array.Some;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.RandomAccess;

import static re.artoo.lance.value.array.Arrayable.merge;

public sealed interface Array<ELEMENT> extends Iterable<ELEMENT>, Iterator<ELEMENT>, RandomAccess permits Empty, Some {
  @SafeVarargs
  static <ELEMENT> Array<ELEMENT> of(ELEMENT... elements) {
    return new Some<>(merge(elements));
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> empty() {
    return (Array<ELEMENT>) Empty.Default;
  }
  @SuppressWarnings("unchecked")
  default Array<ELEMENT> copy(ELEMENT... elements) {
    return Array.of(elements);
  }

  @Override
  default Iterator<ELEMENT> iterator() {
    return this;
  }

  @Override
  default boolean hasNext() {
    return false;
  }

  @Override
  default ELEMENT next() {
    return null;
  }

  @SuppressWarnings("unchecked")
  default Array<ELEMENT> concat(ELEMENT... elements) {
    return Array.of(elements);
  }
  default Array<ELEMENT> concat(Array<ELEMENT> array) {
    return this instanceof Some<ELEMENT> some && array == null
      ? some
      : array;
  }
  default Array<ELEMENT> push(ELEMENT element) {
    return Array.of(element);
  }
  default ELEMENT at(int index) {
    throw new ArrayIndexOutOfBoundsException(index);
  }
}


