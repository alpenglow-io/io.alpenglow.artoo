package re.artoo.lance.query.cursor;

import java.util.Objects;
import java.util.function.Consumer;

public interface BackIterator<ELEMENT> {
  boolean hasPrior();

  ELEMENT prior();

  default void forget() {
    throw new UnsupportedOperationException("forget");
  }

  default void forEachLeaving(Consumer<? super ELEMENT> action) {
    Objects.requireNonNull(action);
    while (hasPrior()) action.accept(prior());
  }
}
