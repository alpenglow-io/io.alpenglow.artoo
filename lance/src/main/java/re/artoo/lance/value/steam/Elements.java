package re.artoo.lance.value.steam;

import java.lang.reflect.Array;
import java.util.Arrays;

import static java.lang.System.arraycopy;

enum Elements {
  Companion;

  @SafeVarargs
  final <ELEMENT> ELEMENT[] concat(ELEMENT[] head, ELEMENT... tail) {
    final var copied = Arrays.copyOf(head, head.length + tail.length);
    arraycopy(tail, 0, copied, head.length, tail.length);
    return copied;
  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  final <ELEMENT> ELEMENT[] asArray(ELEMENT... elements) {
    ELEMENT[] array = (ELEMENT[]) Array.newInstance(elements.getClass().getComponentType(), elements.length);
    if (elements.length > 0) arraycopy(elements, 0, array, 0, elements.length);
    return array;
  }

  @SafeVarargs
  final <ELEMENT> ELEMENT[] copy(int from, ELEMENT... elements) {
    final var copied = Arrays.copyOf(this.<ELEMENT>asArray(), elements.length - 1);
    arraycopy(elements, 1, copied, 0, elements.length - 1);
    return copied;
  }
}
