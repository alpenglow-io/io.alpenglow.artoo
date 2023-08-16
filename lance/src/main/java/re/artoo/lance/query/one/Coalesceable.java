package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;

public interface Coalesceable<ELEMENT> extends Queryable<ELEMENT> {
  default One<ELEMENT> coalesce(TrySupplier1<? extends ELEMENT> vice) {
    return () -> cursor().or(() -> Cursor.lazyValue(vice));
  }

  default One<ELEMENT> coalesce(ELEMENT vice) {
    return () -> cursor().or(() -> Cursor.open(vice));
  }

  default One<ELEMENT> coalesce(One<ELEMENT> vice) {
    return () -> cursor().or(vice::cursor);
  }

  default ELEMENT otherwise(TrySupplier1<? extends ELEMENT> vice) {
    return coalesce(vice).yield();
  }

  default ELEMENT otherwise(ELEMENT vice) {
    return coalesce(vice).yield();
  }

  default ELEMENT otherwise(One<ELEMENT> vice) {
    return coalesce(vice).yield();
  }
}
