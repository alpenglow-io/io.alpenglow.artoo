package re.artoo.lance.value;

import re.artoo.lance.func.TryFunction1;

public sealed interface Steam<ELEMENT> {

  default <TARGET> Steam<TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) throws Throwable {
    return switch (this) {
      case Some<ELEMENT> some -> new Some<>(some.steps()[0].then(unit -> unit.map(operation)));
      case None ignored -> null;
    };
  }
}

record Some<ELEMENT>(Step<ELEMENT>... steps) implements Steam<ELEMENT> { }

enum None implements Steam<Object> { Companion }
