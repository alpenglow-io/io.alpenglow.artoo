package re.artoo.lance.value;

import re.artoo.lance.value.array.Lone;
import re.artoo.lance.value.array.None;
import re.artoo.lance.value.array.Some;

import java.util.*;

import static java.util.Arrays.binarySearch;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public sealed interface Array<ELEMENT> extends Iterable<ELEMENT>, RandomAccess permits Lone, None, Some {
  static <ELEMENT> Array<ELEMENT> of(ELEMENT[] elements) {
    return elements == null || elements.length == 0
      ? none()
      : elements.length == 1
      ? new Lone<>(elements[0])
      : new Some<>(elements);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> of(ELEMENT element, ELEMENT... elements) {
    return element == null && elements != null && elements.length == 0
      ? none()
      : element == null
      ? new Some<>(null, elements)
      : new Some<>(element, elements);
  }

  static <ELEMENT> Array<ELEMENT> of(ELEMENT element) {
    return element != null ? new Lone<>(element) : none();
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> none() {
    return (Array<ELEMENT>) None.Companion;
  }

  @SuppressWarnings("unchecked")
  default Array<ELEMENT> concat(ELEMENT... elements) {
    return Array.of(elements);
  }
  default Array<ELEMENT> concat(Array<ELEMENT> array) {
    return switch (this) {
      case Some<ELEMENT> head when array instanceof Some<ELEMENT> tail -> new Some<>(head.elements(), tail.elements());
      case Some<ELEMENT> head when array instanceof Lone<ELEMENT> tail -> new Some<>(head.elements(), tail.element());
      case Lone<ELEMENT> head when array instanceof Some<ELEMENT> tail -> new Some<>(head.element(), tail.elements());
      case Lone<ELEMENT> head when array instanceof Lone<ELEMENT> tail -> new Some<>(head.element(), tail.element());
      case None ignored when array instanceof Some<ELEMENT> tail -> tail;
      case None ignored when array instanceof Lone<ELEMENT> tail -> tail;
      default -> this;
    };
  }
  @SuppressWarnings("unchecked")
  default Array<ELEMENT> push(ELEMENT... elements) {
    return switch (this) {
      case None ignored when elements.length == 0 -> this;
      case None ignored when elements.length == 1 -> new Lone<>(elements[0]);
      case None ignored -> new Some<>(elements);
      case Some<ELEMENT> ignored when elements.length == 0 -> this;
      case Some<ELEMENT> some -> new Some<>(some.elements(), elements);
      default -> this;
    };
  }

  default Optional<ELEMENT> at(int index) {
    return switch (this) {
      case Some<ELEMENT> some when index >= 0 && some.elements().length > index -> Optional.of(some.elements()[index]);
      case Lone<ELEMENT> lone when index == 0 -> Optional.of(lone.element());
      default -> Optional.empty();
    };
  }

  default Array<ELEMENT> sort() {
    return switch (this) {
      case Some<ELEMENT> some -> new Some<>(sort(some.elements()));
      case Lone<ELEMENT> lone -> lone;
      case None ignored -> this;
    };
  }

  default Array<ELEMENT> sortBy(Comparator<? super ELEMENT> comparison) {
    return switch (this) {
      case Some<ELEMENT> some when comparison != null -> new Some<>(sort(some.elements(), comparison));
      default -> this;
    };
  }

  default boolean includes(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT> some -> binarySearch(some.elements(), element) >= 0;
      default -> false;
    };
  }

  default Optional<ELEMENT> findLast() {
    return switch (this) {
      case Some<ELEMENT> some -> Optional.of(some.elements()[some.elements().length - 1]);
      case Lone<ELEMENT> lone -> Optional.of(lone.element());
      case None none -> Optional.empty();
    };
  }

  default Optional<ELEMENT> findFirst() {
    return switch (this) {
      case Some<ELEMENT> some -> Optional.of(some.elements()[0]);
      case None ignored -> Optional.empty();
      case Lone<ELEMENT> lone -> Optional.of(lone.element());
    };
  }

  default Array<ELEMENT> head() {
    return switch (this) {
      case Some<ELEMENT> some when some.elements().length >= 1 -> new Lone<>(some.elements()[0]);
      case Some<ELEMENT> ignored -> Array.none();
      case Lone<ELEMENT> lone -> lone;
      case None ignored -> this;
    };
  }

  @SuppressWarnings("DuplicateBranchesInSwitch")
  default Array<ELEMENT> tail() {
    return switch (this) {
      case Some<ELEMENT> some when some.elements().length > 1 -> new Some<>(1, some.elements());
      case Some<ELEMENT> ignored -> Array.none();
      case Lone<ELEMENT> ignored -> Array.none();
      case None ignored -> this;
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
      case Some<ELEMENT> some -> some.elements().length;
      case Lone<ELEMENT> ignored -> 1;
      case None ignored -> 0;
    };
  }

  default ELEMENT[] asArray() {
    return switch (this) {
      case Some<ELEMENT> some -> some.elements();
      case Lone<ELEMENT> lone -> lone.elements();
      case None none -> none.elements();
    };
  }

  default List<ELEMENT> asList() {
    return switch (this) {
      case Some<ELEMENT> some -> List.of(some.elements());
      case Lone<ELEMENT> lone -> List.of(lone.element());
      case None none -> List.of();
    };
  }
}


