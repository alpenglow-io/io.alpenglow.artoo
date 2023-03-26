package re.artoo.lance.value;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.value.scope.Clean;
import re.artoo.lance.value.scope.Local;

public sealed interface Scope<SOURCE> permits Local, Clean {
  static <SOURCE> Scope<SOURCE> of(SOURCE source) {
    return new Local<>(source);
  }

  @SuppressWarnings("unchecked")
  private static <SOURCE> Scope<SOURCE> clean() {
    return (Scope<SOURCE>) Clean.Companion;
  }

  default <TARGET> Scope<? extends TARGET> map(TryFunction1<? super SOURCE, ? extends TARGET> operation) {
    return switch (this) {
      case Local<SOURCE> local -> Scope.of(operation.apply(local.element()));
      case Clean ignored -> clean();
    };
  }

  default <TARGET, T extends TARGET> Scope<? extends TARGET> flatMap(TryFunction1<? super SOURCE, ? extends Scope<T>> operation) {
    return switch (this) {
      case Local<SOURCE> local -> operation.apply(local.element()) instanceof Local<T> val ? val : clean();
      case Clean ignored -> clean();
    };
  }

  default Scope<SOURCE> filter(TryPredicate1<? super SOURCE> condition) {
    return switch (this) {
      case Local<SOURCE> local -> condition.test(local.element()) ? local : clean();
      case Clean ignored -> clean();
    };
  }

  default Scope<SOURCE> peek(TryConsumer1<? super SOURCE> operation) {
    return switch (this) {
      case Local<SOURCE> local -> operation.selfAccept(this, local.element());
      default -> this;
    };
  }

  default Scope<SOURCE> or(SOURCE source) {
    return switch (this) {
      case Clean ignored -> new Local<>(source);
      default -> this;
    };
  }

  default <EXCEPTION extends RuntimeException> Scope<SOURCE> or(TrySupplier1<EXCEPTION> exception) {
    return switch (this) {
      case Clean ignored -> throw exception.get();
      default -> this;
    };
  }

  default SOURCE otherwise(SOURCE source) {
    return switch (this) {
      case Local<SOURCE> local -> local.element();
      default -> source;
    };
  }

  default <EXCEPTION extends RuntimeException> SOURCE otherwise(TrySupplier1<EXCEPTION> operation) {
    return switch (this) {
      case Local<SOURCE> local -> local.element();
      default -> throw operation.get();
    };
  }
}

