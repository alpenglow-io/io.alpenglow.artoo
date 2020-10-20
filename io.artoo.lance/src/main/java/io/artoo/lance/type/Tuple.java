package io.artoo.lance.type;

import io.artoo.lance.func.Func;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static io.artoo.lance.type.TupleType.has;
import static io.artoo.lance.type.TupleType.tryComponentOf;

public interface Tuple<R extends Record> {
  Class<R> $type();

  interface Single<R extends Record, A> extends Tuple<R> {
    default A first() { return tryComponentOf(this, $type(), 0); }

    default <T extends Record> T to(final @NotNull Func.Uni<? super A, ? extends T> to) {
      return to.apply(first());
    }

    default <T> T as(final @NotNull Func.Uni<? super A, ? extends T> as) {
      return as.apply(first());
    }

    default boolean is(final A value) {
      return has(first(), value);
    }
  }

  interface Pair<R extends Record, A, B> extends Single<R, A> {
    default B second() { return tryComponentOf(this, $type(), 1); }

    default <T extends Record> T to(final @NotNull Func.Bi<? super A, ? super B, ? extends T> to) {
      return to.apply(first(), second());
    }

    default <T> T as(final @NotNull Func.Bi<? super A, ? super B, ? extends T> as) {
      return as.apply(first(), second());
    }

    default boolean is(final A value1, final B value2) {
      return is(value1) && has(second(), value2);
    }
  }

  interface Triple<R extends Record, A, B, C> extends Pair<R, A, B> {
    default C third() { return tryComponentOf(this, $type(), 2); }

    default <T extends Record> T as(final @NotNull Func.Tri<? super A, ? super B, ? super C, ? extends T> as) {
      return as.apply(first(), second(), third());
    }

    default boolean is(final A value1, final B value2, final C value3) {
      return is(value1, value2) && has(third(), value3);
    }
  }

  interface Quadruple<R extends Record, A, B, C, D> extends Triple<R, A, B, C> {
    default D forth() { return tryComponentOf(this, $type(), 3); }

    default <T extends Record> T as(final @NotNull Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends T> as) {
      return as.apply(first(), second(), third(), forth());
    }

    default boolean is(final A value1, final B value2, final C value3, final D value4) {
      return is(value1, value2, value3) && has(forth(), value4);
    }
  }

  interface Quintuple<R extends Record, A, B, C, D, E> extends Quadruple<R, A, B, C, D> {
    default E fifth() { return tryComponentOf(this, $type(), 4); }

    default <T extends Record> T as(final @NotNull Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends T> as) {
      return as.apply(first(), second(), third(), forth(), fifth());
    }

    default boolean is(final A value1, final B value2, final C value3, final D value4, final E value5) {
      return is(value1, value2, value3, value4) && has(fifth(), value5);
    }
  }
}

@SuppressWarnings("unchecked")
enum TupleType {;
  static <R extends Record, T> @NotNull Optional<T> componentOf(final Object instance, @NotNull final Class<R> type, final int index) {
    try {
      return Optional.ofNullable(
        index >= 0 && type.getRecordComponents().length > index
          ? (T) type.getRecordComponents()[index].getAccessor().invoke(instance)
          : null
      );
    } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
      return Optional.empty();
    }
  }

  static <R extends Record, T> @NotNull T tryComponentOf(final Object instance, @NotNull final Class<R> type, final int index) {
    return TupleType.<R, T>componentOf(instance, type, index).orElseThrow(IllegalStateException::new);
  }

  static <T> boolean has(final T property, final T value) {
    if (property == null && value == null) {
      return true;
    }
    assert property != null;
    return property.equals(value);
  }
}
