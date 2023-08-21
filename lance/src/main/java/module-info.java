module re.artoo.lance {
  requires static java.logging;

  requires jdk.incubator.concurrent;

  exports re.artoo.lance;
  exports re.artoo.lance.func;
  exports re.artoo.lance.value;
  exports re.artoo.lance.query;
  exports re.artoo.lance.query.cursor;
  exports re.artoo.lance.query.many;
  exports re.artoo.lance.query.one;
}
