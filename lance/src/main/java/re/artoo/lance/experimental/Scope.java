package re.artoo.lance.experimental;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.experimental.scope.Clean;
import re.artoo.lance.experimental.scope.Local;

public sealed interface Scope<ELEMENT> permits Local, Clean {
  static <ELEMENT> Scope<ELEMENT> of(ELEMENT element) {
    return new Local<>(element);
  }

  @SuppressWarnings("unchecked")
  private static <ELEMENT> Scope<ELEMENT> clean() {
    return (Scope<ELEMENT>) Clean.Companion;
  }

  default <TARGET, T extends TARGET> Scope<T> map(TryFunction1<? super ELEMENT, T> operation) {
    return switch (this) {
      case Local<ELEMENT>(var it) -> Scope.of(operation.apply(it));
      case Clean ignored -> clean();
    };
  }

  default <TARGET, T extends TARGET> Scope<T> flatMap(TryFunction1<? super ELEMENT, ? extends Scope<T>> operation) {
    return switch (this) {
      case Local<ELEMENT>(var it) when operation.apply(it) instanceof Local<T> val -> val;
      default -> clean();
    };
  }

  default Scope<ELEMENT> filter(TryPredicate1<? super ELEMENT> condition) {
    return switch (this) {
      case Local<ELEMENT>(var it) -> condition.test(it) ? this : clean();
      case Clean ignored -> clean();
    };
  }

  default Scope<ELEMENT> peek(TryConsumer1<? super ELEMENT> operation) {
    return switch (this) {
      case Local<ELEMENT>(var it) -> {
        operation.accept(it);
        yield this;
      }
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
      case Local<ELEMENT>(var it) -> it;
      default -> element;
    };
  }

  default <EXCEPTION extends RuntimeException> ELEMENT otherwise(TrySupplier1<EXCEPTION> operation) {
    return switch (this) {
      case Local<ELEMENT>(var it) -> it;
      default -> throw operation.get();
    };
  }
}

