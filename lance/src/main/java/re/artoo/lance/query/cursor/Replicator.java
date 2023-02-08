package re.artoo.lance.query.cursor;

@SuppressWarnings("unchecked")
public sealed interface Replicator<T, R extends Replicator<T, R>> permits Head {
  default R replicate() { return (R) this; }
}
