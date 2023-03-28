package re.artoo.lance.value;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.value.array.None;
import re.artoo.lance.value.array.Some;

import java.util.*;

import static java.lang.System.arraycopy;
import static jdk.internal.logger.BootstrapLogger.BootstrapExecutors.tail;

public sealed interface Array<ELEMENT> extends Iterable<ELEMENT>, RandomAccess permits None, Some {

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> of(ELEMENT... elements) {
    return elements != null && elements.length == 0
      ? none()
      : new Some<>(elements);
  }

  static <ELEMENT> Array<ELEMENT> ofArray(ELEMENT[] elements) {
    return elements != null && elements.length == 0
      ? none()
      : new Some<>(elements);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> none() {
    return (Array<ELEMENT>) None.Companion;
  }

  default Array<ELEMENT> concat(Array<ELEMENT> array) {
    return switch (this) {
      case Some<ELEMENT>(var head, var ign1) when array instanceof Some<ELEMENT>(var tail, var ign2) -> new Some<>(head, tail);
      default -> array;
    };
  }

  default Array<ELEMENT> push(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT> some -> new Some<>(some.elements(), element);
      case None ignored -> new Some<>(element);
    };
  }

  default Optional<ELEMENT> at(int index) {
    return switch (this) {
      case Some<ELEMENT>(var elements, var start) when index >= 0 && elements.length > index + start -> Optional.of(elements[index]);
      default -> Optional.empty();
    };
  }

  default Array<ELEMENT> sort() {
    return switch (this) {
      case Some<ELEMENT> some -> new Some<>(selfSort(some.elements()));
      case None ignored -> this;
    };
  }

  default Array<ELEMENT> sortBy(Comparator<? super ELEMENT> comparison) {
    return switch (this) {
      case Some<ELEMENT> some when comparison != null -> new Some<>(selfSort(some.elements(), comparison));
      default -> this;
    };
  }

  default boolean includes(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT>(var elements, var ignored) when elements.length > 0 -> elements[0].equals(element) || tail().includes(element);
      default -> false;
    };
  }

  default Optional<ELEMENT> findLast() {
    return switch (this) {
      case Some<ELEMENT> some -> Optional.of(some.elements()[some.elements().length - 1]);
      case None ignored -> Optional.empty();
    };
  }

  default Optional<ELEMENT> findFirst() {
    return switch (this) {
      case Some<ELEMENT> some -> Optional.of(some.elements()[0]);
      case None ignored -> Optional.empty();
    };
  }

  default Array<ELEMENT> head() {
    return switch (this) {
      case Some<ELEMENT>(var elements, var __) when elements.length > 0 -> Array.of(elements[0]);
      case Some<ELEMENT> ignored -> Array.none();
      case None ignored -> this;
    };
  }

  default Array<ELEMENT> tail() {
    return switch (this) {
      case Some<ELEMENT>(var elements) when elements.length > 1 -> new Some<>(1, elements);
      case Some<ELEMENT> ignored -> Array.none();
      case None ignored -> this;
    };
  }

  private ELEMENT[] selfSort(ELEMENT[] elements) {
    Arrays.sort(elements);
    return elements;
  }

  private ELEMENT[] selfSort(ELEMENT[] elements, Comparator<? super ELEMENT> comparison) {
    Arrays.sort(elements, comparison);
    return elements;
  }

  default int length() {
    return switch (this) {
      case Some<ELEMENT>(var elements) -> elements.length;
      case None ignored -> 0;
    };
  }

  @SuppressWarnings("unchecked")
  default <TARGET> Array<TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT>(var head) -> Array.<TARGET>of(operation.apply(head[0])).concat(some.tail().map(operation));
      default -> Array.none();
    };
  }

  /**
   * In order to make it stable, flat-map needs more than such trivial recursive condition, it's not possible for the platform to handle all that sequential concatenations applied
   * on the fly: this involves a continuous computation and reallocation of the final array heap size; what flat-map needs to make it work better, it is to compute from the
   * beginning the necessary capacity for the final array and then to copy all flatten steps.
   *
   * @param operation the condition map that we need to apply
   * @param <TARGET>  the condition target element
   * @param <ARRAY>   the condition target array of steps
   * @return the array
   */
  default <TARGET, ARRAY extends Array<TARGET>> Array<TARGET> flatMap(TryFunction1<? super ELEMENT, ? extends ARRAY> operation) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT>(var head) -> operation.apply(head[0]).concat(some.tail().flatMap(operation));
      default -> Array.none();
    };
  }

  default Array<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && condition.test(head.element()) -> Array.of(head.element()).concat(some.tail().filter(condition));
      default -> Array.none();
    };
  }

  default <TARGET> TARGET fold(TARGET initial, TryFunction2<? super TARGET, ? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> some.tail().fold(operation.apply(initial, head.element()), operation);
      default -> initial;
    };
  }

  default Optional<ELEMENT> reduce(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> Optional.of(some.tail().fold(head.element(), operation));
      default -> Optional.empty();
    };
  }

  default Optional<ELEMENT> find(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && condition.test(head.element()) -> Optional.of(head.element());
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && !condition.test(head.element()) -> some.tail().find(condition);
      default -> Optional.empty();
    };
  }

  default boolean every(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head -> condition.test(head.element()) && some.tail().every(condition);
      default -> true;
    };
  }

  default int indexOf(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && head.element().equals(element) -> 0;
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && !head.element().equals(element) -> some.tail().indexOf(1, element);
      default -> -1;
    };
  }

  private int indexOf(int index, ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && head.element().equals(element) -> index;
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head && !head.element().equals(element) -> some.tail().indexOf(++index, element);
      default -> -1;
    };
  }

  default String join() {
    return join(',');
  }

  default String join(char separator) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head ->
        some.tail().fold(new StringBuilder(head.element().toString()), (builder, element) -> builder.append("%c%s".formatted(separator, element))).toString();
      default -> "";
    };
  }

  default String join(String separator) {
    return switch (this) {
      case Some<ELEMENT> some when some.head() instanceof Some<ELEMENT> head ->
        some.tail().fold(new StringBuilder(head.element().toString()), (builder, element) -> builder.append("%s%s".formatted(separator, element))).toString();
      default -> "";
    };
  }

  default Cursor<ELEMENT> cursor() {
    return switch (this) {
      case Some<ELEMENT> some -> Cursor.open(some.elements());
      default -> Cursor.empty();
    };
  }

  default ELEMENT[] copyTo(TryIntFunction<ELEMENT[]> array) {
    return switch (this) {
      case Some<ELEMENT> some -> {
        ELEMENT[] dest = array.apply(some.elements().length);
        arraycopy(some.elements(), 0, dest, 0, some.elements().length);
        yield dest;
      }
      case None none -> none.elements();
    };
  }

  default List<ELEMENT> toList() {
    return switch (this) {
      case Some<ELEMENT> some -> List.of(some.elements());
      case None ignored -> List.of();
    };
  }
}


