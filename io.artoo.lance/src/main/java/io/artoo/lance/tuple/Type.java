package io.artoo.lance.tuple;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

enum Type {
  ;

  @SuppressWarnings("unchecked")
  static <R extends Record, T> @NotNull T componentOf(final Object instance, @NotNull final Class<R> type, final int index) {
    try {
      assert index >= 0 && type.getRecordComponents().length > index;

      return (T) type
        .getRecordComponents()[index]
        .getAccessor()
        .invoke(instance);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new NominalTupleException("Can't invoke nominal tuple %s component of index %d.".formatted(type.getSimpleName(), index), e);
    }
  }

  static <R extends Record, T> @NotNull T firstOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 0); }

  static <R extends Record, T> @NotNull T secondOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 1); }

  static <R extends Record, T> @NotNull T thirdOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 2); }

  static <R extends Record, T> @NotNull T forthOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 3); }

  static <R extends Record, T> @NotNull T fifthOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 4); }

  static <R extends Record, T> @NotNull T sixthOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 5); }

  static <R extends Record, T> @NotNull T seventhOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 6); }

  static <R extends Record, T> @NotNull T eighthOf(final Object instance, final Class<R> type) { return componentOf(instance, type, 7); }

  static <T> boolean has(final T property, final T value) {
    if (property == null && value == null) {
      return true;
    }
    assert property != null;
    return property.equals(value);
  }
}
