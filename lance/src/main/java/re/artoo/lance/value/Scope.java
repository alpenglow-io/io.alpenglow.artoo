package re.artoo.lance.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.unit.Empty;
import re.artoo.lance.value.unit.Value;

public sealed interface Scope<ELEMENT> permits Value, Empty {
  static <ELEMENT> Scope<ELEMENT> of(ELEMENT element) {
    return new Value<>(element);
  }

  @SuppressWarnings("unchecked")
  private static <ELEMENT> Scope<ELEMENT> empty() {
    return (Scope<ELEMENT>) Empty.Companion;
  }

  default <TARGET> Scope<? extends TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Value<ELEMENT> value -> Scope.of(operation.apply(value.element()));
      case Empty ignored -> empty();
    };
  }

  default <TARGET> Scope<? extends TARGET> flatMap(TryFunction1<? super ELEMENT, ? extends Scope<? extends TARGET>> operation) {
    return switch (this) {
      case Value<ELEMENT> value -> {
        var applied = operation.apply(value.element());
        yield applied instanceof Value<?> ignored ? applied : empty();
      }
      case Empty ignored -> empty();
    };
  }

  default Scope<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Value<ELEMENT> value -> condition.test(value.element()) ? value : empty();
      case Empty ignored -> empty();
    };
  }

  default Scope<ELEMENT> peek(TryConsumer1<? super ELEMENT> operation) {
    return switch (this) {
      case Value<ELEMENT> value -> operation.selfAccept(this, value.element());
      default -> this;
    };
  }

  default Scope<ELEMENT> or(ELEMENT element) {
    return switch (this) {
      case Empty ignored -> new Value<>(element);
      default -> this;
    };
  }

  default <EXCEPTION extends RuntimeException> Scope<ELEMENT> or(TrySupplier1<EXCEPTION> exception) {
    return switch (this) {
      case Empty ignored -> throw exception.get();
      default -> this;
    };
  }

  default ELEMENT otherwise(ELEMENT element) {
    return switch (this) {
      case Value<ELEMENT> value -> value.element();
      default -> element;
    };
  }

  default <EXCEPTION extends RuntimeException> ELEMENT otherwise(TrySupplier1<? extends EXCEPTION> operation) {
    return switch (this) {
      case Value<ELEMENT> value -> value.element();
      default -> throw operation.get();
    };
  }
}

