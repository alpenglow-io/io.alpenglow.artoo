package oak.quill.query;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("SpellCheckingInspection")
interface Customers {
  @NotNull
  @Contract(value = " -> new", pure = true)
  static Customer[] customers() {
    return new Customer[]{
      new Customer(1, "Alfreds,  Futterkiste ", "Maria Anders ", "Obere Str. 57 ", "Berlin ", "12209 ", "Germany"),
      new Customer(2, "Ana,  Trujillo Emparedados y helados ", "Ana Trujillo ", "Avda. de la Constitución 2222 ", "México D.F. ", "05021 ", "Mexico"),
      new Customer(3, "Antonio,  Moreno Taquería ", "Antonio Moreno ", "Mataderos 2312 ", "México D.F. ", "05023 ", "Mexico"),
      new Customer(4, "Around,  the Horn ", "Thomas Hardy ", "120 Hanover Sq. ", "London ", "WA1 1DP ", "UK"),
      new Customer(5, "Berglunds,  snabbköp ", "Christina Berglund ", "Berguvsvägen 8 ", "Luleå ", "S-958 22 ", "Sweden"),
      new Customer(6, "Blauer,  See Delikatessen ", "Hanna Moos ", "Forsterstr. 57 ", "Mannheim ", "68306 ", "Germany"),
      new Customer(7, "Blondel,  père et fils ", "Frédérique Citeaux ", "24, place Kléber ", "Strasbourg ", "67000 ", "France"),
      new Customer(8, "Bólido,  Comidas preparadas ", "Martín Sommer ", "C/ Araquil, 67 ", "Madrid ", "28023 ", "Spain"),
      new Customer(9, "Bon,  app' ", "Laurence Lebihans ", "12, rue des Bouchers ", "Marseille ", "13008 ", "France"),
      new Customer(10, "Bottom-Dollar Marketse ", "Elizabeth Lincoln ", "23 Tsawassen Blvd. ", "Tsawassen ", "T2F 8M4 ", "Canada"),
      new Customer(11, "B's Beverages ", "Victoria Ashworth ", "Fauntleroy Circus ", "London ", "EC2 5NT ", "UK"),
      new Customer(12, "Cactus Comidas para llevar ", "Patricio Simpson ", "Cerrito 333 ", "Buenos Aires ", "1010 ", "Argentina"),
      new Customer(13, "Centro comercial Moctezuma ", "Francisco Chang ", "Sierras de Granada 9993 ", "México D.F. ", "05022 ", "Mexico"),
      new Customer(14, "Chop-suey Chinese ", "Yang Wang ", "Hauptstr. 29 ", "Bern ", "3012 ", "Switzerland"),
      new Customer(15, "Comércio Mineiro ", "Pedro Afonso ", "Av. dos Lusíadas, 23 ", "São Paulo ", "05432-043 ", "Brazil"),
      new Customer(16, "Consolidated Holdings ", "Elizabeth Brown ", "Berkeley Gardens 12 Brewery  ", "London ", "WX1 6LT ", "UK"),
      new Customer(17, "Drachenblut Delikatessend ", "Sven Ottlieb ", "Walserweg 21 ", "Aachen ", "52066 ", "Germany"),
      new Customer(18, "Du monde entier ", "Janine Labrune ", "67, rue des Cinquante Otages ", "Nantes ", "44000 ", "France"),
      new Customer(19, "Eastern Connection ", "Ann Devon ", "35 King George ", "London ", "WX3 6FW ", "UK"),
      new Customer(20, "Ernst Handel ", "Roland Mendel ", "Kirchgasse 6 ", "Graz ", "8010 ", "Austria"),
      new Customer(21, "Familia Arquibaldo ", "Aria Cruz ", "Rua Orós, 92 ", "São Paulo ", "05442-030 ", "Brazil"),
      new Customer(22, "FISSA Fabrica Inter. Salchichas S.A. ", "Diego Roel ", "C/ Moralzarzal, 86 ", "Madrid ", "28034 ", "Spain"),
      new Customer(23, "Folies gourmandes ", "Martine Rancé ", "184, chaussée de Tournai ", "Lille ", "59000 ", "France"),
      new Customer(24, "Folk och fä HB ", "Maria Larsson ", "Åkergatan 24 ", "Bräcke ", "S-844 67 ", "Sweden"),
      new Customer(25, "Frankenversand ", "Peter Franken ", "Berliner Platz 43 ", "München ", "80805 ", "Germany"),
      new Customer(26, "France restauration ", "Carine Schmitt ", "54, rue Royale ", "Nantes ", "44000 ", "France"),
      new Customer(27, "Franchi S.p.A. ", "Paolo Accorti ", "Via Monte Bianco 34 ", "Torino ", "10100 ", "Italy"),
      new Customer(28, "Furia Bacalhau e Frutos do Mar ", "Lino Rodriguez  ", "Jardim das rosas n. 32 ", "Lisboa ", "1675 ", "Portugal"),
      new Customer(29, "Galería del gastrónomo ", "Eduardo Saavedra ", "Rambla de Cataluña, 23 ", "Barcelona ", "08022 ", "Spain"),
      new Customer(30, "Godos Cocina Típica ", "José Pedro Freyre ", "C/ Romero, 33 ", "Sevilla ", "41101 ", "Spain"),
      new Customer(31, "Gourmet Lanchonetes ", "André Fonseca ", "Av. Brasil, 442 ", "Campinas ", "04876-786 ", "Brazil"),
      new Customer(32, "Great Lakes Food Market ", "Howard Snyder ", "2732 Baker Blvd. ", "Eugene ", "97403 ", "USA"),
      new Customer(33, "GROSELLA-Restaurante ", "Manuel Pereira ", "5ª Ave. Los Palos Grandes ", "Caracas ", "1081 ", "Venezuela"),
      new Customer(34, "Hanari Carnes ", "Mario Pontes ", "Rua do Paço, 67 ", "Rio de Janeiro ", "05454-876 ", "Brazil"),
      new Customer(35, "HILARIÓN-Abastos ", "Carlos Hernández ", "Carrera 22 con Ave. Carlos Soublette #8-35 ", "San Cristóbal ", "5022 ", "Venezuela"),
      new Customer(36, "Hungry Coyote Import Store ", "Yoshi Latimer ", "City Center Plaza 516 Main St. ", "Elgin ", "97827 ", "USA"),
      new Customer(37, "Hungry Owl All-Night Grocers ", "Patricia McKenna ", "8 Johnstown Road ", "Cork ", " ", "Ireland"),
      new Customer(38, "Island Trading ", "Helen Bennett ", "Garden House Crowther Way ", "Cowes ", "PO31 7PJ ", "UK"),
      new Customer(39, "Königlich Essen ", "Philip Cramer ", "Maubelstr. 90 ", "Brandenburg ", "14776 ", "Germany"),
      new Customer(40, "La corne d'abondance ", "Daniel Tonini ", "67, avenue de l'Europe ", "Versailles ", "78000 ", "France"),
      new Customer(41, "La maison d'Asie ", "Annette Roulet ", "1 rue Alsace-Lorraine ", "Toulouse ", "31000 ", "France"),
      new Customer(42, "Laughing Bacchus Wine Cellars ", "Yoshi Tannamuri ", "1900 Oak St. ", "Vancouver ", "V3F 2K1 ", "Canada"),
      new Customer(43, "Lazy K Kountry Store ", "John Steel ", "12 Orchestra Terrace ", "Walla Walla ", "99362 ", "USA"),
      new Customer(44, "Lehmanns Marktstand ", "Renate Messner ", "Magazinweg 7 ", "Frankfurt a.M.  ", "60528 ", "Germany"),
      new Customer(45, "Let's Stop N Shop ", "Jaime Yorres ", "87 Polk St. Suite 5 ", "San Francisco ", "94117 ", "USA"),
      new Customer(46, "LILA-Supermercado ", "Carlos González ", "Carrera 52 con Ave. Bolívar #65-98 Llano Largo ", "Barquisimeto ", "3508 ", "Venezuela"),
      new Customer(47, "LINO-Delicateses ", "Felipe Izquierdo ", "Ave. 5 de Mayo Porlamar ", "I. de Margarita ", "4980 ", "Venezuela"),
      new Customer(48, "Lonesome Pine Restaurant ", "Fran Wilson ", "89 Chiaroscuro Rd. ", "Portland ", "97219 ", "USA"),
      new Customer(49, "Magazzini Alimentari Riuniti ", "Giovanni Rovelli ", "Via Ludovico il Moro 22 ", "Bergamo ", "24100 ", "Italy"),
      new Customer(50, "Maison Dewey ", "Catherine Dewey ", "Rue Joseph-Bens 532 ", "Bruxelles ", "B-1180 ", "Belgium"),
      new Customer(51, "Mère Paillarde ", "Jean Fresnière ", "43 rue St. Laurent ", "Montréal ", "H1J 1C3 ", "Canada"),
      new Customer(52, "Morgenstern Gesundkost ", "Alexander Feuer ", "Heerstr. 22 ", "Leipzig ", "04179 ", "Germany"),
      new Customer(53, "North/South ", "Simon Crowther ", "South House 300 Queensbridge ", "London ", "SW7 1RZ ", "UK"),
      new Customer(54, "Océano Atlántico Ltda. ", "Yvonne Moncada ", "Ing. Gustavo Moncada 8585 Piso 20-A ", "Buenos Aires ", "1010 ", "Argentina"),
      new Customer(55, "Old World Delicatessen ", "Rene Phillips ", "2743 Bering St. ", "Anchorage ", "99508 ", "USA"),
      new Customer(56, "Ottilies Käseladen ", "Henriette Pfalzheim ", "Mehrheimerstr. 369 ", "Köln ", "50739 ", "Germany"),
      new Customer(57, "Paris spécialités ", "Marie Bertrand ", "265, boulevard Charonne ", "Paris ", "75012 ", "France"),
      new Customer(58, "Pericles Comidas clásicas ", "Guillermo Fernández ", "Calle Dr. Jorge Cash 321 ", "México D.F. ", "05033 ", "Mexico"),
      new Customer(59, "Piccolo und mehr ", "Georg Pipps ", "Geislweg 14 ", "Salzburg ", "5020 ", "Austria"),
      new Customer(60, "Princesa Isabel Vinhoss ", "Isabel de Castro ", "Estrada da saúde n. 58 ", "Lisboa ", "1756 ", "Portugal"),
      new Customer(61, "Que Delícia ", "Bernardo Batista ", "Rua da Panificadora, 12 ", "Rio de Janeiro ", "02389-673 ", "Brazil"),
      new Customer(62, "Queen Cozinha ", "Lúcia Carvalho ", "Alameda dos Canàrios, 891 ", "São Paulo ", "05487-020 ", "Brazil"),
      new Customer(63, "QUICK-Stop ", "Horst Kloss ", "Taucherstraße 10 ", "Cunewalde ", "01307 ", "Germany"),
      new Customer(64, "Rancho grande ", "Sergio Gutiérrez ", "Av. del Libertador 900 ", "Buenos Aires ", "1010 ", "Argentina"),
      new Customer(65, "Rattlesnake Canyon Grocery ", "Paula Wilson ", "2817 Milton Dr. ", "Albuquerque ", "87110 ", "USA"),
      new Customer(66, "Reggiani Caseifici ", "Maurizio Moroni ", "Strada Provinciale 124 ", "Reggio Emilia ", "42100 ", "Italy"),
      new Customer(67, "Ricardo Adocicados ", "Janete Limeira ", "Av. Copacabana, 267 ", "Rio de Janeiro ", "02389-890 ", "Brazil"),
      new Customer(68, "Richter Supermarkt ", "Michael Holz ", "Grenzacherweg 237 ", "Genève ", "1203 ", "Switzerland"),
      new Customer(69, "Romero y tomillo ", "Alejandra Camino ", "Gran Vía, 1 ", "Madrid ", "28001 ", "Spain"),
      new Customer(70, "Santé Gourmet ", "Jonas Bergulfsen ", "Erling Skakkes gate 78 ", "Stavern ", "4110 ", "Norway"),
      new Customer(71, "Save-a-lot Markets ", "Jose Pavarotti ", "187 Suffolk Ln. ", "Boise ", "83720 ", "USA"),
      new Customer(72, "Seven Seas Imports ", "Hari Kumar ", "90 Wadhurst Rd. ", "London ", "OX15 4NB ", "UK"),
      new Customer(73, "Simons bistro ", "Jytte Petersen ", "Vinbæltet 34 ", "København ", "1734 ", "Denmark"),
      new Customer(74, "Spécialités du monde ", "Dominique Perrier ", "25, rue Lauriston ", "Paris ", "75016 ", "France"),
      new Customer(75, "Split Rail Beer & Ale ", "Art Braunschweiger ", "P.O. Box 555 ", "Lander ", "82520 ", "USA"),
      new Customer(76, "Suprêmes délices ", "Pascale Cartrain ", "Boulevard Tirou, 255 ", "Charleroi ", "B-6000 ", "Belgium"),
      new Customer(77, "The Big Cheese ", "Liz Nixon ", "89 Jefferson Way Suite 2 ", "Portland ", "97201 ", "USA"),
      new Customer(78, "The Cracker Box ", "Liu Wong ", "55 Grizzly Peak Rd. ", "Butte ", "59801 ", "USA"),
      new Customer(79, "Toms Spezialitäten ", "Karin Josephs ", "Luisenstr. 48 ", "Münster ", "44087 ", "Germany"),
      new Customer(80, "Tortuga Restaurante ", "Miguel Angel Paolino ", "Avda. Azteca 123 ", "México D.F. ", "05033 ", "Mexico"),
      new Customer(81, "Tradição Hipermercados ", "Anabela Domingues ", "Av. Inês de Castro, 414 ", "São Paulo ", "05634-030 ", "Brazil"),
      new Customer(82, "Trail's Head Gourmet Provisioners ", "Helvetius Nagy ", "722 DaVinci Blvd. ", "Kirkland ", "98034 ", "USA"),
      new Customer(83, "Vaffeljernet ", "Palle Ibsen ", "Smagsløget 45 ", "Århus ", "8200 ", "Denmark"),
      new Customer(84, "Victuailles en stock ", "Mary Saveley ", "2, rue du Commerce ", "Lyon ", "69004 ", "France"),
      new Customer(85, "Vins et alcools Chevalier ", "Paul Henriot ", "59 rue de l'Abbaye ", "Reims ", "51100 ", "France"),
      new Customer(86, "Die Wandernde Kuh ", "Rita Müller ", "Adenauerallee 900 ", "Stuttgart ", "70563 ", "Germany"),
      new Customer(87, "Wartian Herkku ", "Pirkko Koskitalo ", "Torikatu 38 ", "Oulu ", "90110 ", "Finland"),
      new Customer(88, "Wellington Importadora ", "Paula Parente ", "Rua do Mercado, 12 ", "Resende ", "08737-363 ", "Brazil"),
      new Customer(89, "White Clover Markets ", "Karl Jablonski ", "305 - 14th Ave. S. Suite 3B ", "Seattle ", "98128 ", "USA"),
      new Customer(90, "Wilman Kala ", "Matti Karttunen ", "Keskuskatu 45 ", "Helsinki ", "21240 ", "Finland"),
      new Customer(91, "Wolski ", "Zbyszek ", "ul. Filtrowa 68 ", "Walla ", "01-012 ", "Poland")
    };
  }
}

final class Customer {
  final long id;
  final String name;
  final String contact;
  final String address;
  final String city;
  final String postalCode;
  final String country;

  @Contract(pure = true)
  Customer(final long id, final String name, final String contact, final String address, final String city, final String postalCode, final String country) {
    this.id = id;
    this.name = name;
    this.contact = contact;
    this.address = address;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }
}
