module io.artoo.lance {
  //noinspection JavaRequiresAutoModule
  requires static transitive org.jetbrains.annotations;

  requires static io.vertx.core;

  exports io.artoo.lance.cursor;
  exports io.artoo.lance.fetcher;
  exports io.artoo.lance.func;
  exports io.artoo.lance.scope;
  exports io.artoo.lance.query;
}
