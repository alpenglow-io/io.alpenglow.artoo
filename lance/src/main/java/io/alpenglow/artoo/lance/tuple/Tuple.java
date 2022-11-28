package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.tuple.record.OfFive;
import io.alpenglow.artoo.lance.tuple.record.OfFour;
import io.alpenglow.artoo.lance.tuple.record.OfThree;
import io.alpenglow.artoo.lance.tuple.record.OfTwo;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public interface Tuple {
  static <A, B> Pair<A, B> ofTwoEmpty() { return (Pair<A, B>) OfTwo.Empty.Default; }
  static <A, B> Pair<A, B> of(final A first, final B second) {
    return new OfTwo<>(first, second);
  }

  static <A, B, C> Triple<A, B, C> of(final A first, final B second, final C third) {
    return new OfThree<>(first, second, third);
  }

  static <A, B, C, D> Quadruple<A, B, C, D> of(final A first, final B second, final C third, final D forth) {
    return new OfFour<>(first, second, third, forth);
  }

  static <A, B, C, D, E> Quintuple<A, B, C, D, E> of(final A first, final B second, final C third, final D forth, E fifth) {
    return new OfFive<>(first, second, third, forth, fifth);
  }

  default Many<?> asQueryable() {
    try {
      return switch (this) {
        case Pair<?, ?> it -> Many.fromAny(it.first(), it.second());
        case Triple<?, ?, ?> it -> Many.fromAny(it.first(), it.second(), it.third());
        case Quadruple<?, ?, ?, ?> it -> Many.fromAny(it.first(), it.second(), it.third(), it.forth());
        case Quintuple<?, ?, ?, ?, ?> it -> Many.fromAny(it.first(), it.second(), it.third(), it.forth(), it.fifth());
        case Record it -> {
          final var type = it.getClass();
          final var elements = new Object[type.getRecordComponents().length];
          final var components = type.getRecordComponents();

          for (var index = 0; index < components.length - 1; index++) {
            final var method = components[index].getAccessor();
            if (!method.canAccess(this)) method.setAccessible(true);
            elements[index] = method.invoke(this);
          }

          yield Many.fromAny(elements);
        }
        default -> Many.empty();
      };
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new NominalTupleException("Can't invoke nominal tuple %s component of index %d.".formatted(this.getClass().getSimpleName(), 0), e);
    }
  }
}
