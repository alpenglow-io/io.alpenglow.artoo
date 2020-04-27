package artoo.sample;

import artoo.sample.currency.Currencies;

import static java.lang.System.out;
import static artoo.sample.Main.DefaultCurrency.Dollar;
import static artoo.sample.Main.DefaultCurrency.Lira;
import static artoo.sample.currency.Currency.Entry;
import static artoo.sample.currency.Currency.Id;
import static artoo.sample.currency.Currency.Name;

public enum Main {
  ;

  public static void main(String... args) {
    final var lira = new Entry(
      Id.from("lira")
        .or("lira can't be an Id.", RuntimeException::new)
        .asIs(),
      Lira,
      () -> 1000.0
    );
    final var dollaro = new Entry(
      Id.from("dollaro").asIs(),
      Dollar,
      () -> 10.0
    );

    final Id dollar = Id.from("dollaro").asIs();
    Currencies.from(lira, dollaro)
      .one(dollar)
      .eventually(out::println);
  }

  enum DefaultCurrency implements Name {
    Lira("Lira"), Dollar("Dollar"), Pound("Pound");

    private final String name;

    DefaultCurrency(final String name) {
      this.name = name;
    }

    @Override
    public final String eval() {
      return name;
    }
  }
}
