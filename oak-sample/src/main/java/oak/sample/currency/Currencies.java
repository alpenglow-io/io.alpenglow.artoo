package oak.sample.currency;

import oak.cursor.Cursor;
import oak.func.Pred;
import oak.query.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.many(entries);
  }

  default Currency one(final Currency.Id id) {
    return this
      .single(entry -> entry.id().is(id))
      .as(Currency::from);
  }
}

interface IdPredicate extends Pred<Currency.Id> {}
