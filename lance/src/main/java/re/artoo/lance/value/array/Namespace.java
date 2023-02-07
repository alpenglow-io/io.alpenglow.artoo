package re.artoo.lance.value.array;

import java.util.Arrays;

import static java.lang.System.arraycopy;

enum Namespace {
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
}
