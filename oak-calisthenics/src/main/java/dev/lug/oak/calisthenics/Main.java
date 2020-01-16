package dev.lug.oak.calisthenics;

import dev.lug.oak.calisthenics.currency.Currencies;
import dev.lug.oak.calisthenics.currency.Currency;

import static dev.lug.oak.calisthenics.Main.CurrencyName.Dollaro;
import static dev.lug.oak.calisthenics.Main.CurrencyName.Lira;
import static dev.lug.oak.calisthenics.Main.CurrencyName.Sterlina;

public interface Main {
  enum Numbers {
    Int(Integer.class, 12), Decimal(Double.class, 12.0);

    private final Class<?> clazz;
    private final Number number;

    Numbers(Class<?> clazz, Number number) {
      this.clazz = clazz;
      this.number = number;
    }

    @SuppressWarnings("unchecked")
    public Object number() {
      return clazz.cast(number);
    }
  }

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
    final var lira = new Currency.Entry(
      () -> "asd",
      Lira,
      () -> 1000.0
    );
    final var dollaro = new Currency.Entry(
      () -> "dsa",
      Dollaro,
      () -> 10.0
    );

    Currencies.from(lira, dollaro).one(() -> "dsa")
      .or("Dollaro has not been found.", IllegalArgumentException::new)
      .eventually(currency -> currency.replace(Sterlina, () -> 50.0));
  }
}
