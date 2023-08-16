package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

public interface Triggerable<ELEMENT> extends Queryable<ELEMENT> {
  default Case<ELEMENT> fire(TryConsumer1<? super ELEMENT> operation) {
    return fire((__, it) -> operation.invoke(it));
  }

  default Case<ELEMENT> fire(TryIntConsumer1<? super ELEMENT> operation) {
    return condition -> () -> Triggerable.this.cursor().filter(condition).peek(operation);
  }

  default Case<ELEMENT> raise(String message, TryFunction1<? super String, ? extends Throwable> operation) {
    return condition -> () -> Triggerable.this.cursor().filter(condition.oppose()).or(message, operation);
  }

  default Case<ELEMENT> raise(TrySupplier1<? extends Throwable> operation) {
    return raise(null, ignored -> operation.invoke());
  }
}

