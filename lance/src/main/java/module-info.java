module io.alpenglow.artoo.lance {
  requires static java.logging;

  exports io.alpenglow.artoo.lance.func;
  exports io.alpenglow.artoo.lance.func.tail;
  exports io.alpenglow.artoo.lance.literator;
  exports io.alpenglow.artoo.lance.literator.cursor;
  exports io.alpenglow.artoo.lance.literator.cursor.routine;
  exports io.alpenglow.artoo.lance.query;
  exports io.alpenglow.artoo.lance.query.many;
  exports io.alpenglow.artoo.lance.query.one;
  exports io.alpenglow.artoo.lance.tuple;
  exports io.alpenglow.artoo.lance.value;
  exports io.alpenglow.artoo.lance.query.func;
  exports io.alpenglow.artoo.lance.scope;
  exports io.alpenglow.artoo.lance;
}
