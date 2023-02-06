package re.artoo.lance.value.array;

import java.util.Arrays;

import static java.lang.System.arraycopy;
import static re.artoo.lance.value.array.Namespace.Companion;

@SuppressWarnings("unchecked")
public interface Arrangement {
  default <ELEMENT> ELEMENT[] merge(ELEMENT[] head, ELEMENT... tail) {
    return Companion.merge(head, tail);
  }

  default <ELEMENT> ELEMENT[] asArray(ELEMENT... elements) {
    return elements;
  }
}

