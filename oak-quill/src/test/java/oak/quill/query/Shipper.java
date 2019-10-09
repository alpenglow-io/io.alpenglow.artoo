package oak.quill.query;

import org.jetbrains.annotations.Contract;

interface Shippers {
  static Shipper[] shippers() {
    return new Shipper[]{
      new Shipper(1, "Speedy Express", "(503) 555-9831"),
      new Shipper(2, "United Package", "(503) 555-3199"),
      new Shipper(3, "Federal Shipping", "(503) 555-9931")
    };
  }
}

final class Shipper {
  final long id;
  final String name;
  final String phone;

  @Contract(pure = true)
  Shipper(final long id, final String name, final String phone) {
    this.id = id;
    this.name = name;
    this.phone = phone;
  }
}
