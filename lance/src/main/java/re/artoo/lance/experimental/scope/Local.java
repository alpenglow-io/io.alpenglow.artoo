package re.artoo.lance.experimental.scope;

import re.artoo.lance.experimental.Scope;

public record Local<ELEMENT>(ELEMENT element) implements Scope<ELEMENT> {
}
