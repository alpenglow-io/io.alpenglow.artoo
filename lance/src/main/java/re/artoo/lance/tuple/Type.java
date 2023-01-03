package re.artoo.lance.tuple;

import java.lang.reflect.InvocationTargetException;

enum Type {
  ;

  @SuppressWarnings("unchecked")
  static <T> T componentOf(final Object instance, final int index) {
    final var type = instance.getClass();
    try {
      if (instance instanceof Record it) {
        final var method = type
          .getRecordComponents()[index]
          .getAccessor();
        if (!method.canAccess(it)) method.setAccessible(true);
        return (T) method.invoke(it);
      }
      throw new NominalTupleException("Can't invoke component %d of nominal tuple %s, since .".formatted(index, type.getSimpleName()));
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new NominalTupleException("Can't invoke component %d of nominal tuple %s.".formatted(index, type.getSimpleName()), e);
    }
  }

  static <T> T sixthOf(final Object instance) { return componentOf(instance, 5); }

  static <T> T seventhOf(final Object instance) { return componentOf(instance, 6); }

  static <T> T eighthOf(final Object instance) { return componentOf(instance, 7); }

  static <T> T ninthOf(final Object instance) { return componentOf(instance, 8); }

  static <T> boolean has(final T property, final T value) {
    if (property == null && value == null) {
      return true;
    }
    assert property != null;
    return property.equals(value);
  }

}
