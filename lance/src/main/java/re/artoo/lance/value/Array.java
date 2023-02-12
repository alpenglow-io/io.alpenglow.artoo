package re.artoo.lance.value;

import re.artoo.lance.value.array.Copied;
import re.artoo.lance.value.array.Empty;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

import static java.util.Arrays.*;

public sealed interface Array<ELEMENT> extends Iterable<ELEMENT>, RandomAccess permits Empty, Copied {
  static <ELEMENT> Array<ELEMENT> of(ELEMENT[] elements) {
    return of(null, elements);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> of(ELEMENT element, ELEMENT... elements) {
    return element == null && elements != null && elements.length == 0
      ? empty()
      : element == null
      ? new Copied<>(null, elements)
      : new Copied<>(element, elements);
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
      case Copied<ELEMENT> head when array instanceof Copied<ELEMENT> tail -> new Copied<>(head.elements(), tail.elements());
      case Empty head when array instanceof Copied<ELEMENT> tail -> tail;
      default -> this;
    };
  }
  default Array<ELEMENT> push(ELEMENT element) {
    return switch (this) {
      case Copied<ELEMENT> head when element != null -> new Copied<>(head.elements(), element);
      case Empty head when element != null -> new Copied<>(element);
      default -> this;
    };
  }
  default ELEMENT at(int index) {
    return switch (this) {
      case Copied<ELEMENT> array when index >= 0 && array.elements().length > index -> array.elements()[index];
      case Empty array when index >= 0 -> throw new IndexOutOfBoundsException();
      default -> throw new NoSuchElementException();
    };
  }

  default Array<ELEMENT> sort() {
    return switch (this) {
      case Copied<ELEMENT> array -> new Copied<>(sort(array.elements()));
      case Empty array -> this;
    };
  }

  default Array<ELEMENT> sortBy(Comparator<? super ELEMENT> comparison) {
    return switch (this) {
      case Copied<ELEMENT> array when comparison != null -> new Copied<>(sort(array.elements(), comparison));
      default -> this;
    };
  }

  default boolean includes(ELEMENT element) {
    return switch (this) {
      case Copied<ELEMENT> array -> binarySearch(array.elements(), element) >= 0;
      default -> false;
    };
  }

  default ELEMENT findLast() {
    return switch (this) {
      case Copied<ELEMENT> array -> array.elements()[array.length() - 1];
      default -> null;
    };
  }

  default ELEMENT findFirst() {
    return switch (this) {
      case Copied<ELEMENT> array -> array.elements()[0];
      default -> null;
    };
  }

  private ELEMENT[] sort(ELEMENT[] elements) {
    Arrays.sort(elements);
    return elements;
  }

  private ELEMENT[] sort(ELEMENT[] elements, Comparator<? super ELEMENT> comparison) {
    Arrays.sort(elements, comparison);
    return elements;
  }

  default int length() {
    return switch (this) {
      case Copied<ELEMENT> array -> array.elements().length;
      case Empty array -> 0;
    };
  }
}


