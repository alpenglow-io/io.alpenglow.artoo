package re.artoo.lance.value.array;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public interface Arrayable {
  @SafeVarargs
  static <ELEMENT> ELEMENT[] merge(ELEMENT[] head, ELEMENT... tail) {
    final var copied = Arrays.copyOf(head, head.length + tail.length);
    arraycopy(head, 0, copied, head.length, tail.length);
    return copied;
  }
}
