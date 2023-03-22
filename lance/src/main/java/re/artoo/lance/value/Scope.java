package re.artoo.lance.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.unit.Clean;
import re.artoo.lance.value.unit.Local;

public sealed interface Scope<ELEMENT> permits Local, Clean {
  static <ELEMENT> Scope<ELEMENT> of(ELEMENT element) {
    return new Local<>(element);
  }

  @SuppressWarnings("unchecked")
  private static <ELEMENT> Scope<ELEMENT> clean() {
    return (Scope<ELEMENT>) Clean.Companion;
  }

  default <TARGET> Scope<? extends TARGET> map(TryFunction1<? super ELEMENT, ? extends TARGET> operation) {
    return switch (this) {
      case Local<ELEMENT> local -> Scope.of(operation.apply(local.element()));
      case Clean ignored -> clean();
    };
  }

  default <TARGET, T extends TARGET> Scope<? extends TARGET> flatMap(TryFunction1<? super ELEMENT, ? extends Scope<T>> operation) {
    return switch (this) {
      case Local<ELEMENT> local -> operation.apply(local.element()) instanceof Local<T> val ? val : clean();
      case Clean ignored -> clean();
    };
  }

  default Scope<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Local<ELEMENT> local -> condition.test(local.element()) ? local : clean();
      case Clean ignored -> clean();
    };
  }

  default Scope<ELEMENT> peek(TryConsumer1<? super ELEMENT> operation) {
    return switch (this) {
      case Local<ELEMENT> local -> operation.selfAccept(this, local.element());
      default -> this;
    };
  }

  default Scope<ELEMENT> or(ELEMENT element) {
    return switch (this) {
      case Clean ignored -> new Local<>(element);
      default -> this;
    };
  }

  default <EXCEPTION extends RuntimeException> Scope<ELEMENT> or(TrySupplier1<EXCEPTION> exception) {
    return switch (this) {
      case Clean ignored -> throw exception.get();
      default -> this;
    };
  }

  default ELEMENT otherwise(ELEMENT element) {
    return switch (this) {
      case Local<ELEMENT> local -> local.element();
      default -> element;
    };
  }

  default <EXCEPTION extends RuntimeException> ELEMENT otherwise(TrySupplier1<? extends EXCEPTION> operation) {
    return switch (this) {
      case Local<ELEMENT> local -> local.element();
      default -> throw operation.get();
    };
  }
}

