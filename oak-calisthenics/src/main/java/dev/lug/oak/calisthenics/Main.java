package dev.lug.oak.calisthenics;

import dev.lug.oak.calisthenics.currency.Currencies;
import dev.lug.oak.calisthenics.currency.Currency;

import static dev.lug.oak.calisthenics.Main.CurrencyName.Dollaro;
import static dev.lug.oak.calisthenics.Main.CurrencyName.Lira;
import static dev.lug.oak.calisthenics.Main.CurrencyName.Sterlina;

public interface Main {
  enum CurrencyName implements Currency.Name {
    Lira("Lira"), Dollaro("Dollaro"), Sterlina("Sterlina");

    private final String name;

    CurrencyName(final String name) {
      this.name = name;
    }

    @Override
    public final String eval() {
      return name;
    }
  }

  static void main(String... args) {
    final var lira = Currency.of(
      () -> "asd",
      Lira,
      () -> 1000.0
    );
    final var dollaro = Currency.of(
      () -> "dsa",
      Dollaro,
      () -> 10.0
    );

    Currencies.of(lira, dollaro).findBy(() -> "dsa")
      .or("Dollaro has not been found.", IllegalArgumentException::new)
      .eventually(currency -> currency.set(Sterlina, () -> 50.0));
  }
}
