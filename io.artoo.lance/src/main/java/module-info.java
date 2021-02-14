module io.artoo.lance {
  requires static org.jetbrains.annotations;
  requires static io.vertx.core;
  requires io.netty.common;
  requires io.netty.transport;

  exports io.artoo.lance.cursor;
  exports io.artoo.lance.fetcher;
  exports io.artoo.lance.func;
  exports io.artoo.lance.scope;
  exports io.artoo.lance.query;
}
