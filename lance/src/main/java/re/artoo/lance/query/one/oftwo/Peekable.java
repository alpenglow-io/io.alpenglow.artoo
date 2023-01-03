package re.artoo.lance.query.one.oftwo;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryConsumer2;
import re.artoo.lance.func.TryConsumer3;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Peek;

public interface Peekable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> peek(TryConsumer2<? super A, ? super B> peek) {
    return peek((index, first, second) -> peek.invoke(first, second));
  }

  default One.OfTwo<A, B> peek(TryConsumer3<? super Integer, ? super A, ? super B> peek) {
    return () -> cursor().map(new Peek<>((index, record) -> peek.invoke(index, record.first(), record.second())));
  }

  default One.OfTwo<A, B> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return () -> cursor().exceptionally(catch$);
  }
}

