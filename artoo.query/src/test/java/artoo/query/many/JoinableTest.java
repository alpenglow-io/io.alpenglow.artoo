package artoo.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import artoo.query.Many;

import static org.assertj.core.api.Assertions.assertThat;
import static artoo.query.many.TestData.CUSTOMERS;
import static artoo.query.many.TestData.ORDERS;
import static artoo.query.many.TestData.SHIPPERS;

class JoinableTest {
  @Test
  @DisplayName("should join each pet with its owner")
  void shouldJoinPetsWithItsOwner() {
    final var magnus = new Person("Hedlund, Magnus");
    final var terry = new Person("Adams, Terry");
    final var charlotte = new Person("Weiss, Charlotte");

    final var barley = new Pet("Barley", terry);
    final var boots = new Pet("Boots", terry);
    final var whiskers = new Pet("Whiskers", charlotte);
    final var daisy = new Pet("Daisy", magnus);

    final var query = Many.from(magnus, terry, charlotte)
      .join(barley, boots, whiskers, daisy).on((person, pet) -> person.equals(pet.owner()))
      .select(arm -> arm.as((person, pet) ->
        String.format("%s - %s",
          person.name().trim(),
          pet.name().trim()
        )
      ));

    assertThat(query).contains(
      "Hedlund, Magnus - Daisy",
      "Adams, Terry - Barley",
      "Adams, Terry - Boots",
      "Weiss, Charlotte - Whiskers"
    );
  }

  @Test
  @DisplayName("should show for every order what shipper has been selected by customer")
  void shouldShowForEveryOrderWhatShipperHasBeenSelectedByCustomer() {
    record ShippedOrder(
      long id,
      String customerName,
      long shipperId) {
    }

    final var query = Many.from(ORDERS)
      .join(CUSTOMERS).on((order, customer) -> order.customerId() == customer.id())
      .select(bag -> bag.as((order, customer) -> new ShippedOrder(
        order.id(),
        customer.name(),
        order.shipperId()))
      )
      .join(SHIPPERS).on((order, shipper) -> order.shipperId() == shipper.id())
      .select(arm -> arm.as((order, shipper) ->
        String.format("%s;%s",
          order.customerName().trim(),
          shipper.name().trim()
        ).trim()
      ));

    assertThat(query).contains(
      "Comércio Mineiro;Speedy Express",
      "Lehmanns Marktstand;Speedy Express",
      "Seven Seas Imports;Speedy Express",
      "Ernst Handel;Speedy Express",
      "LILA-Supermercado;Speedy Express",
      "Split Rail Beer & Ale;Speedy Express",
      "Hungry Owl All-Night Grocers;Speedy Express",
      "Rattlesnake Canyon Grocery;Speedy Express",
      "Que Delícia;Speedy Express",
      "Reggiani Caseifici;Speedy Express",
      "Magazzini Alimentari Riuniti;Speedy Express",
      "LINO-Delicateses;Speedy Express",
      "Queen Cozinha;Speedy Express",
      "Folies gourmandes;Speedy Express",
      "Océano Atlántico Ltda.;Speedy Express",
      "HILARIÓN-Abastos;Speedy Express",
      "Eastern Connection;Speedy Express",
      "Reggiani Caseifici;Speedy Express",
      "Ernst Handel;Speedy Express",
      "Lehmanns Marktstand;Speedy Express",
      "Around the Horn;Speedy Express",
      "La maison d'Asie;Speedy Express",
      "Ernst Handel;Speedy Express",
      "Bon app';Speedy Express",
      "Princesa Isabel Vinhoss;Speedy Express",
      "LILA-Supermercado;Speedy Express",
      "Folk och fä HB;Speedy Express",
      "Save-a-lot Markets;Speedy Express",
      "Königlich Essen;Speedy Express",
      "La maison d'Asie;Speedy Express",
      "Lonesome Pine Restaurant;Speedy Express",
      "Bon app';Speedy Express",
      "White Clover Markets;Speedy Express",
      "Tradição Hipermercados;Speedy Express",
      "Victuailles en stock;Speedy Express",
      "Wartian Herkku;Speedy Express",
      "Ernst Handel;Speedy Express",
      "Old World Delicatessen;Speedy Express",
      "Blondel père et fils;Speedy Express",
      "Ernst Handel;Speedy Express",
      "Frankenversand;Speedy Express",
      "Reggiani Caseifici;Speedy Express",
      "Romero y tomillo;Speedy Express",
      "Galería del gastrónomo;Speedy Express",
      "Wartian Herkku;Speedy Express",
      "Franchi S.p.A.;Speedy Express",
      "Que Delícia;Speedy Express",
      "Romero y tomillo;Speedy Express",
      "QUICK-Stop;Speedy Express",
      "Berglunds snabbköp;Speedy Express",
      "Hungry Coyote Import Store;Speedy Express",
      "Vins et alcools Chevalier;Speedy Express",
      "Magazzini Alimentari Riuniti;Speedy Express",
      "Wellington Importadora;Speedy Express",
      "Victuailles en stock;United Package",
      "Que Delícia;United Package",
      "Bólido Comidas preparadas;United Package",
      "Split Rail Beer & Ale;United Package",
      "QUICK-Stop;United Package",
      "Wellington Importadora;United Package",
      "Chop-suey Chinese;United Package",
      "Berglunds snabbköp;United Package",
      "Mère Paillarde;United Package",
      "Hungry Owl All-Night Grocers;United Package",
      "Hanari Carnes;United Package",
      "Princesa Isabel Vinhoss;United Package",
      "Mère Paillarde;United Package",
      "Suprêmes délices;United Package",
      "Frankenversand;United Package",
      "White Clover Markets;United Package",
      "Hanari Carnes;United Package",
      "Lehmanns Marktstand;United Package",
      "Tortuga Restaurante;United Package",
      "Rattlesnake Canyon Grocery;United Package",
      "Rattlesnake Canyon Grocery;United Package",
      "Blondel père et fils;United Package",
      "Hungry Owl All-Night Grocers;United Package",
      "Ricardo Adocicados;United Package",
      "Split Rail Beer & Ale;United Package",
      "Magazzini Alimentari Riuniti;United Package",
      "Tradição Hipermercados;United Package",
      "Die Wandernde Kuh;United Package",
      "Island Trading;United Package",
      "Godos Cocina Típica;United Package",
      "The Big Cheese;United Package",
      "Lonesome Pine Restaurant;United Package",
      "Que Delícia;United Package",
      "Vins et alcools Chevalier;United Package",
      "Die Wandernde Kuh;United Package",
      "QUICK-Stop;United Package",
      "Rattlesnake Canyon Grocery;United Package",
      "Island Trading;United Package",
      "QUICK-Stop;United Package",
      "Island Trading;United Package",
      "Suprêmes délices;United Package",
      "Mère Paillarde;United Package",
      "Bottom-Dollar Marketse;United Package",
      "Split Rail Beer & Ale;United Package",
      "Chop-suey Chinese;United Package",
      "Queen Cozinha;United Package",
      "Piccolo und mehr;United Package",
      "Ernst Handel;United Package",
      "La maison d'Asie;United Package",
      "Ernst Handel;United Package",
      "Hungry Coyote Import Store;United Package",
      "Hungry Owl All-Night Grocers;United Package",
      "Richter Supermarkt;United Package",
      "La maison d'Asie;United Package",
      "Wartian Herkku;United Package",
      "Split Rail Beer & Ale;United Package",
      "Santé Gourmet;United Package",
      "Bottom-Dollar Marketse;United Package",
      "Die Wandernde Kuh;United Package",
      "Ottilies Käseladen;United Package",
      "Mère Paillarde;United Package",
      "Folk och fä HB;United Package",
      "Toms Spezialitäten;United Package",
      "Galería del gastrónomo;United Package",
      "Antonio Moreno Taquería;United Package",
      "QUICK-Stop;United Package",
      "Split Rail Beer & Ale;United Package",
      "Save-a-lot Markets;United Package",
      "Ernst Handel;United Package",
      "Old World Delicatessen;United Package",
      "La maison d'Asie;United Package",
      "Blondel père et fils;United Package",
      "Consolidated Holdings;United Package",
      "Die Wandernde Kuh;United Package",
      "Mère Paillarde;Federal Shipping",
      "Bottom-Dollar Marketse;Federal Shipping",
      "Bottom-Dollar Marketse;Federal Shipping",
      "Simons bistro;Federal Shipping",
      "QUICK-Stop;Federal Shipping",
      "Ricardo Adocicados;Federal Shipping",
      "Ernst Handel;Federal Shipping",
      "B's Beverages;Federal Shipping",
      "LILA-Supermercado;Federal Shipping",
      "Gourmet Lanchonetes;Federal Shipping",
      "Folk och fä HB;Federal Shipping",
      "Rattlesnake Canyon Grocery;Federal Shipping",
      "Wartian Herkku;Federal Shipping",
      "Drachenblut Delikatessend;Federal Shipping",
      "Princesa Isabel Vinhoss;Federal Shipping",
      "Morgenstern Gesundkost;Federal Shipping",
      "HILARIÓN-Abastos;Federal Shipping",
      "Familia Arquibaldo;Federal Shipping",
      "QUICK-Stop;Federal Shipping",
      "Richter Supermarkt;Federal Shipping",
      "Ernst Handel;Federal Shipping",
      "Tortuga Restaurante;Federal Shipping",
      "GROSELLA-Restaurante;Federal Shipping",
      "Wartian Herkku;Federal Shipping",
      "Centro comercial Moctezuma;Federal Shipping",
      "Frankenversand;Federal Shipping",
      "Wartian Herkku;Federal Shipping",
      "Pericles Comidas clásicas;Federal Shipping",
      "Königlich Essen;Federal Shipping",
      "Vaffeljernet;Federal Shipping",
      "Furia Bacalhau e Frutos do Mar;Federal Shipping",
      "Drachenblut Delikatessend;Federal Shipping",
      "Wartian Herkku;Federal Shipping",
      "Blondel père et fils;Federal Shipping",
      "Save-a-lot Markets;Federal Shipping",
      "LILA-Supermercado;Federal Shipping",
      "Wolski;Federal Shipping",
      "Old World Delicatessen;Federal Shipping",
      "Bon app';Federal Shipping",
      "Simons bistro;Federal Shipping",
      "Pericles Comidas clásicas;Federal Shipping",
      "Piccolo und mehr;Federal Shipping",
      "Wilman Kala;Federal Shipping",
      "Furia Bacalhau e Frutos do Mar;Federal Shipping",
      "Familia Arquibaldo;Federal Shipping",
      "Seven Seas Imports;Federal Shipping",
      "Romero y tomillo;Federal Shipping",
      "Vaffeljernet;Federal Shipping",
      "Tortuga Restaurante;Federal Shipping",
      "Save-a-lot Markets;Federal Shipping",
      "Frankenversand;Federal Shipping",
      "Hungry Coyote Import Store;Federal Shipping",
      "Rattlesnake Canyon Grocery;Federal Shipping",
      "Piccolo und mehr;Federal Shipping",
      "Familia Arquibaldo;Federal Shipping",
      "Tortuga Restaurante;Federal Shipping",
      "Old World Delicatessen;Federal Shipping",
      "Hungry Owl All-Night Grocers;Federal Shipping",
      "Around the Horn;Federal Shipping",
      "Ana Trujillo Emparedados y helados;Federal Shipping",
      "LILA-Supermercado;Federal Shipping",
      "Hungry Owl All-Night Grocers;Federal Shipping",
      "Du monde entier;Federal Shipping",
      "Folk och fä HB;Federal Shipping",
      "Seven Seas Imports;Federal Shipping",
      "Rattlesnake Canyon Grocery;Federal Shipping",
      "Eastern Connection;Federal Shipping",
      "Berglunds snabbköp;Federal Shipping"
    );
  }
}
