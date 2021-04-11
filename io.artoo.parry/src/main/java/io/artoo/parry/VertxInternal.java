package io.artoo.parry;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.PromiseInternal;
import io.artoo.parry.eventloop.EventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public interface VertxInternal extends Vertx {

  /**
   * @return a promise associated with the context returned by {@link #getOrCreateContext()}.
   */
  <T> PromiseInternal<T> promise();

  /**
   * @return a promise associated with the context returned by {@link #getOrCreateContext()} or the {@code handler}
   *         if that handler is already an instance of {@code PromiseInternal}
   */
  <T> PromiseInternal<T> promise(Handler<AsyncResult<T>> handler);

  long maxEventLoopExecTime();

  TimeUnit maxEventLoopExecTimeUnit();

  @Override
  ContextInternal getOrCreateContext();

  EventLoopGroup getEventLoopGroup();

  EventLoopGroup getAcceptorEventLoopGroup();

  ExecutorService getWorkerPool();

  /**
   * Get the current context
   * @return the context
   */
  ContextInternal getContext();

  EventLoopContext createEventLoopContext(EventLoop eventLoop, WorkerPool workerPool, ClassLoader tccl);

  EventLoopContext createEventLoopContext();

  /**
   * @return worker loop context
   */
  WorkerContext createWorkerContext(Deployment deployment, CloseFuture closeFuture, WorkerPool pool, ClassLoader tccl);

  WorkerContext createWorkerContext();

  @Override
  WorkerExecutorInternal createSharedWorkerExecutor(String name);

  @Override
  WorkerExecutorInternal createSharedWorkerExecutor(String name, int poolSize);

  @Override
  WorkerExecutorInternal createSharedWorkerExecutor(String name, int poolSize, long maxExecuteTime);

  @Override
  WorkerExecutorInternal createSharedWorkerExecutor(String name, int poolSize, long maxExecuteTime, TimeUnit maxExecuteTimeUnit);

  WorkerPool createSharedWorkerPool(String name, int poolSize, long maxExecuteTime, TimeUnit maxExecuteTimeUnit);

  void simulateKill();

  Deployment getDeployment(String deploymentID);

  void failoverCompleteHandler(FailoverCompleteHandler failoverCompleteHandler);

  boolean isKilled();

  void failDuringFailover(boolean fail);

  File resolveFile(String fileName);

  /**
   * Like {@link #executeBlocking(Handler, Handler)} but using the internal worker thread pool.
   */
  <T> void executeBlockingInternal(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler);

  <T> void executeBlockingInternal(Handler<Promise<T>> blockingCodeHandler, boolean ordered, Handler<AsyncResult<T>> resultHandler);

  ClusterManager getClusterManager();

  HAManager haManager();

  /**
   * Resolve an address (e.g. {@code vertx.io} into the first found A (IPv4) or AAAA (IPv6) record.
   *
   * @param hostname the hostname to resolve
   * @param resultHandler the result handler
   */
  void resolveAddress(String hostname, Handler<AsyncResult<InetAddress>> resultHandler);

  /**
   * @return the address resolver
   */
  AddressResolver addressResolver();

  /**
   * @return the Netty {@code AddressResolverGroup} to use in a Netty {@code Bootstrap}
   */
  AddressResolverGroup<InetSocketAddress> nettyAddressResolverGroup();

  BlockedThreadChecker blockedThreadChecker();

  CloseFuture closeFuture();

  void addCloseHook(Closeable hook);

  void removeCloseHook(Closeable hook);

}
