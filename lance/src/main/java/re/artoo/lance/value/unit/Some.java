package re.artoo.lance.value.unit;

import re.artoo.lance.value.Unit;

public record Some<ELEMENT>(ELEMENT element) implements Unit<ELEMENT> {
}
