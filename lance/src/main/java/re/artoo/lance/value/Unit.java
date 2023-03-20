package re.artoo.lance.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.unit.None;
import re.artoo.lance.value.unit.Some;

public sealed interface Unit<ELEMENT> permits Some, None {
  static <ELEMENT> Unit<ELEMENT> of(ELEMENT element) {
    return new Some<>(element);
  }

  @SuppressWarnings("unchecked")
  private static <ELEMENT> Unit<ELEMENT> none() {
    return (Unit<ELEMENT>) None.Companion;
  }

  default <TARGET> Unit<TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Some<ELEMENT> some -> Unit.of(operation.apply(some.element()));
      case None ignored -> none();
    };
  }

  default <TARGET> Unit<? extends TARGET> flatMap(TryFunction1<? super ELEMENT, ? extends Unit<? extends TARGET>> operation) {
    return switch (this) {
      case Some<ELEMENT> some -> {
        var applied = operation.apply(some.element());
        yield applied instanceof Some<?> ignored ? applied : none();
      }
      case None ignored -> none();
    };
  }

  default Unit<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Some<ELEMENT> some -> condition.test(some.element()) ? some : none();
      case None ignored -> none();
    };
  }

  default Unit<ELEMENT> peek(TryConsumer1<? super ELEMENT> operation) {
    return switch (this) {
      case Some<ELEMENT> some -> operation.selfAccept(this, some.element());
      default -> this;
    };
  }

  default Unit<ELEMENT> or(ELEMENT element) {
    return switch (this) {
      case None ignored -> new Some<>(element);
      default -> this;
    };
  }

  default <EXCEPTION extends RuntimeException> Unit<ELEMENT> or(TrySupplier1<? extends EXCEPTION> operation) {
    return switch (this) {
      case None ignored -> throw operation.get();
      default -> this;
    };
  }

  default ELEMENT otherwise(ELEMENT element) {
    return switch (this) {
      case Some<ELEMENT> some -> some.element();
      default -> element;
    };
  }

  default <EXCEPTION extends RuntimeException> ELEMENT otherwise(TrySupplier1<? extends EXCEPTION> operation) {
    return switch (this) {
      case Some<ELEMENT> some -> some.element();
      default -> throw operation.get();
    };
  }
}

