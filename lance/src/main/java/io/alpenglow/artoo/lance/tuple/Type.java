package io.alpenglow.artoo.lance.tuple;

import io.alpenglow.artoo.lance.query.Many;

import java.lang.reflect.InvocationTargetException;

enum Type {
  ;

  @SuppressWarnings("unchecked")
  static <T> T componentOf(final Object instance, final int index) {
    final var type = instance.getClass();
    try {
      assert index >= 0 && type.isRecord() && type.getRecordComponents().length > index;

      final var method = type
        .getRecordComponents()[index]
        .getAccessor();
      if (!method.canAccess(instance)) method.setAccessible(true);
      return (T) method.invoke(instance);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new NominalTupleException("Can't invoke nominal tuple %s component of index %d.".formatted(type.getSimpleName(), index), e);
    }
  }

  static <T> T firstOf(final Object instance) { return componentOf(instance, 0); }

  static <T> T secondOf(final Object instance) { return componentOf(instance, 1); }

  static <T> T thirdOf(final Object instance) { return componentOf(instance, 2); }

  static <T> T forthOf(final Object instance) { return componentOf(instance, 3); }

  static <T> T fifthOf(final Object instance) { return componentOf(instance, 4); }

  static <T> T sixthOf(final Object instance) { return componentOf(instance, 5); }

  static <T> T seventhOf(final Object instance) { return componentOf(instance, 6); }

  static <T> T eighthOf(final Object instance) { return componentOf(instance, 7); }

  static <T> boolean has(final T property, final T value) {
    if (property == null && value == null) {
      return true;
    }
    assert property != null;
    return property.equals(value);
  }

  static Many<?> asMany(final Object instance) {
    final var type = instance.getClass();
    var index = 0;
    try {
      if (!type.isRecord()) return Many.empty();

      final var elements = new Object[type.getRecordComponents().length];
      final var components = type.getRecordComponents();

      for (var i = 0; i < components.length; index = ++i) {
        final var method = components[i].getAccessor();
        if (!method.canAccess(instance)) method.setAccessible(true);
        elements[i] = method.invoke(instance);
      }

      return Many.fromAny(elements);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new NominalTupleException("Can't invoke nominal tuple %s component of index %d.".formatted(type.getSimpleName(), index), e);
    }
  }
}
