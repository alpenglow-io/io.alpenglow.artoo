package re.artoo.lance.value.array;

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

  @SafeVarargs
  final <ELEMENT> ELEMENT[] asArray(ELEMENT... elements) {
    return elements;
  }

  @SafeVarargs
  final <ELEMENT> ELEMENT[] copy(int from, ELEMENT... elements) {
    final var copied = Arrays.copyOf(this.<ELEMENT>asArray(), elements.length - 1);
    arraycopy(elements, 1, copied, 0, elements.length - 1);
    return copied;
  }
}
