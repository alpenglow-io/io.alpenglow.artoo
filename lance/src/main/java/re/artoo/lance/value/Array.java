package re.artoo.lance.value;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.value.array.None;
import re.artoo.lance.value.array.Some;

import java.util.*;
import java.util.function.UnaryOperator;

import static java.lang.System.arraycopy;
import static java.lang.reflect.Array.newInstance;

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

  @SafeVarargs
  static <ELEMENT> Array<ELEMENT> of(UnaryOperator<Array<ELEMENT>> then, ELEMENT... elements) {
    return then.apply(Array.of(elements));
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> Array<ELEMENT> none() {
    return (Array<ELEMENT>) None.Companion;
  }

  default Array<ELEMENT> concat(Array<ELEMENT> array) {
    return switch (array) {
      case Some<ELEMENT>(var elements) -> concat(elements);
      default -> this;
    };
  }

  default Array<ELEMENT> concat(ELEMENT[] elements) {
    return switch (this) {
      case Some<ELEMENT>(var some) when elements != null && elements.length > 0 -> {
        final var copied = Arrays.copyOf(some, some.length + elements.length);
        arraycopy(elements, 0, copied, some.length, elements.length);
        yield Array.of(copied);
      }
      default -> Array.of(elements);
    };
  }

  default Array<ELEMENT> push(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> {
        var copied = Arrays.copyOf(some, some.length + 1);
        copied[some.length] = element;
        yield Array.of(copied);
      }
      default -> Array.of(element);
    };
  }

  default Optional<ELEMENT> at(int index) {
    return switch (this) {
      case Some<ELEMENT>(var elements) when index >= 0 && elements.length > index -> Optional.of(elements[index]);
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
      case Some<ELEMENT>(var elements) -> elements[0].equals(element) || tail().includes(element);
      case None ignored -> false;
    };
  }

  default Optional<ELEMENT> findLast() {
    return switch (this) {
      case Some<ELEMENT>(var some) -> Optional.of(some[some.length - 1]);
      case None ignored -> Optional.empty();
    };
  }

  default Optional<ELEMENT> findFirst() {
    return switch (this) {
      case Some<ELEMENT>(var some) -> Optional.of(some[0]);
      default -> Optional.empty();
    };
  }

  default Array<ELEMENT> head() {
    return switch (this) {
      case Some<ELEMENT>(var some) -> Array.of(some[0]);
      default -> this;
    };
  }

  @SuppressWarnings("unchecked")
  default Array<ELEMENT> tail() {
    return switch (this) {
      case Some<ELEMENT>(var some) -> {
        var copied = ((Object) some.getClass() == (Object) Object[].class)
          ? (ELEMENT[]) new Object[some.length - 1]
          : (ELEMENT[]) newInstance(some.getClass().getComponentType(), some.length - 1);
        System.arraycopy(some, 1, copied, 0, Math.min(some.length, some.length - 1));
        yield Array.of(copied);
      }
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

  default <TARGET> Array<TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return map(0, operation);
  }

  private <TARGET> Array<TARGET> map(final int index, TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Some<ELEMENT>(var some) when index < some.length -> Array.of(it -> it.concat(map(index + 1, operation)), operation.apply(some[index]));
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
  default <TARGET, ARRAY extends Array<TARGET>> Array<TARGET> flatMap(TryFunction1<? super ELEMENT, ARRAY> operation) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> operation.apply(some[0]).concat(tail().flatMap(operation));
      default -> Array.none();
    };
  }

  default Array<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT>(var some) when condition.test(some[0]) -> Array.of(some[0]).concat(tail().filter(condition));
      default -> Array.none();
    };
  }

  default <TARGET> TARGET fold(TARGET initial, TryFunction2<? super TARGET, ? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> tail().fold(operation.apply(initial, some[0]), operation);
      default -> initial;
    };
  }

  default <TARGET> TARGET yield(TARGET initial, TryConsumer2<? super TARGET, ? super ELEMENT> operation) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> {
        operation.accept(initial, some[0]);
        yield tail().yield(initial, operation);
      }
      default -> initial;
    };
  }

  default Optional<ELEMENT> reduce(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> Optional.of(tail().fold(some[0], operation));
      default -> Optional.empty();
    };
  }

  default Optional<ELEMENT> find(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT>(var some) when condition.test(some[0]) -> Optional.of(some[0]);
      case Some<ELEMENT>(var some) when !condition.test(some[0]) -> tail().find(condition);
      default -> Optional.empty();
    };
  }

  default boolean every(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> condition.test(some[0]) && tail().every(condition);
      default -> true;
    };
  }

  default int indexOf(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT>(var some) when some[0].equals(element) -> 0;
      case Some<ELEMENT>(var some) when !some[0].equals(element) -> tail().indexOf(1, element);
      default -> -1;
    };
  }

  private int indexOf(int index, ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT>(var some) when some[0].equals(element) -> index;
      case Some<ELEMENT>(var some) when !some[0].equals(element) -> tail().indexOf(++index, element);
      default -> -1;
    };
  }

  default String join() {
    return join(',');
  }

  default String join(char separator) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> tail()
        .fold(new StringBuilder(some[0].toString()), (builder, element) -> builder.append("%c%s".formatted(separator, element)))
        .toString();
      default -> "";
    };
  }

  default String join(String separator) {
    return switch (this) {
      case Some<ELEMENT>(var some) -> tail()
        .fold(new StringBuilder(some[0].toString()), (builder, element) -> builder.append("%s%s".formatted(separator, element)))
        .toString();
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
      case Some<ELEMENT>(var some) -> {
        var dest = array.apply(some.length);
        arraycopy(some, 0, dest, 0, some.length);
        yield dest;
      }
      case None none -> none.elements();
    };
  }

  default List<ELEMENT> toList() {
    return switch (this) {
      case Some<ELEMENT>(var some) -> List.of(some);
      case None ignored -> List.of();
    };
  }
}
