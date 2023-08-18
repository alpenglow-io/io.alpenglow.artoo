package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Many;

final class ExhaustibleCondition<ELEMENT> implements TryIntPredicate1<ELEMENT> {
  private final TryIntPredicate1<? super ELEMENT> condition;
  private boolean exhausted;

  ExhaustibleCondition(TryIntPredicate1<? super ELEMENT> condition) {
    this.condition = condition;
  }

  @Override
  public boolean invoke(int index, ELEMENT element) throws Throwable {
    return !(exhausted |= !exhausted && !condition.invoke(index, element));
  }
}

public interface Partitionable<ELEMENT> extends Queryable<ELEMENT> {
  default Many<ELEMENT> skip(int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<ELEMENT> skipWhile(TryPredicate1<? super ELEMENT> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<ELEMENT> skipWhile(TryIntPredicate1<? super ELEMENT> where) {
    return () -> cursor().filter(new ExhaustibleCondition<>(where).oppose());
  }

  default Many<ELEMENT> take(int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<ELEMENT> takeWhile(TryPredicate1<? super ELEMENT> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<ELEMENT> takeWhile(TryIntPredicate1<? super ELEMENT> where) {
    return () -> cursor().filter(new TryIntPredicate1<>() {
        private boolean exhausted;

        @Override
        public boolean invoke(int index, ELEMENT element) throws Throwable {
            return !(exhausted |= !exhausted && !where.invoke(index, element));
        }
    });
  }
}
