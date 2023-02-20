package re.artoo.lance.query.cursor.operation;

final class Ticked<ELEMENT> {
  ELEMENT element;
  private int index;
  private Ticked() {}
  public static <ELEMENT> Ticked<ELEMENT> empty() {
    Ticked<ELEMENT> ticked = new Ticked<>();
    ticked.element = null;
    ticked.index = 0;
    return ticked;
  }
}
