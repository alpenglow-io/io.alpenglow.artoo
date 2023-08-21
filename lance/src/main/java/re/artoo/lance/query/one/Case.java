package re.artoo.lance.query.one;

import re.artoo.lance.func.TryBooleanSupplier;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Case<T> {
  default One<T> when(TryPredicate1<? super T> condition) {
    return when((__, element) -> condition.invoke(element));
  }

  default One<T> when(TryBooleanSupplier condition) {
    return when((__, ___) -> condition.invoke());
  }

  One<T> when(TryIntPredicate1<? super T> condition);
}
