package oak.sample.currency;

import oak.cursor.Cursor;
import oak.func.Pred;
import oak.query.Many;
import oak.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static oak.query.one.Projectable.as;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.many(entries);
  }

  default One<Currency> one(final Currency.Id id) {
    return this
      .single(entry -> entry.id.is(id))
      .select(as(Currency::from));
  }
}

interface IdPredicate extends Pred<Currency.Id> {}
