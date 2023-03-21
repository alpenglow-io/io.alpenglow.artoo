package re.artoo.lance.value.unit;

import re.artoo.lance.value.Scope;

public record Value<ELEMENT>(ELEMENT element) implements Scope<ELEMENT> {
}
