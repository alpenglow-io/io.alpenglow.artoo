package re.artoo.lance.query.many;

import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Many;

@FunctionalInterface
public interface Case<T> {
  default Many<T> when(TryPredicate1<? super T> condition) {
    return when((__, element) -> condition.invoke(element));
  }

  Many<T> when(TryIntPredicate1<? super T> condition);
}
