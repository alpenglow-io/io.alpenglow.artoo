package re.artoo.lance.value.scope;

import re.artoo.lance.value.Scope;

public record Local<ELEMENT>(ELEMENT element) implements Scope<ELEMENT> {
}
