package re.artoo.lance.value.unit;

import re.artoo.lance.value.Scope;

public record Local<ELEMENT>(ELEMENT element) implements Scope<ELEMENT> {
}
