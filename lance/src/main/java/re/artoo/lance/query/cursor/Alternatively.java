package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Er;
import re.artoo.lance.query.cursor.operation.Eventually;
import re.artoo.lance.query.cursor.operation.Exceptionally;
import re.artoo.lance.query.cursor.operation.Or;

public sealed interface Alternatively<ELEMENT> extends Fetchable<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> or(TrySupplier1<? extends Fetchable<ELEMENT>> vice) {
    return new Or<>(this, vice);
  }

  default Cursor<ELEMENT> or(final String message, final TryFunction1<? super String, ? extends Throwable> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<ELEMENT> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Exceptionally<>(this, (__, throwable) -> catch$.invoke(throwable));
  }

  default Cursor<ELEMENT> exceptionally(TryIntConsumer1<? super Throwable> catch$) {
    return new Exceptionally<>(this, catch$);
  }

  default Cursor<ELEMENT> eventually(TryFunction1<? super Throwable, ? extends ELEMENT> recover) {
    return new Eventually<>(this, (__, throwable) -> recover.invoke(throwable));
  }

  default Cursor<ELEMENT> eventually(TrySupplier1<? extends ELEMENT> recover) {
    return new Eventually<>(this, (__, ___) -> recover.invoke());
  }
}

