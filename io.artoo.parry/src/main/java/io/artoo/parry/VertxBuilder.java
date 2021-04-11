/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.artoo.parry;

import io.vertx.core.*;
import io.vertx.core.file.impl.FileResolver;
import io.vertx.core.json.JsonObject;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.net.impl.transport.Transport;
import io.vertx.core.spi.ExecutorServiceFactory;
import io.vertx.core.spi.VertxMetricsFactory;
import io.vertx.core.spi.VertxServiceProvider;
import io.vertx.core.spi.VertxThreadFactory;
import io.vertx.core.spi.VertxTracerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeSelector;
import io.vertx.core.spi.cluster.impl.DefaultNodeSelector;
import io.vertx.core.spi.metrics.VertxMetrics;
import io.vertx.core.spi.tracing.VertxTracer;
import io.vertx.core.tracing.TracingOptions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Vertx builder for creating vertx instances with SPI overrides.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxBuilder {

  private VertxOptions options;
  private VertxThreadFactory threadFactory;
  private ExecutorServiceFactory executorServiceFactory;

  public VertxBuilder(VertxOptions options) {
    this.options = options;
  }

  public VertxBuilder() {
    this(new VertxOptions());
  }

  /**
   * @return the vertx options
   */
  public VertxOptions options() {
    return options;
  }

  /**
   * @return the {@code VertxThreadFactory} to use
   */
  public VertxThreadFactory threadFactory() {
    return threadFactory;
  }

  /**
   * Set the {@code VertxThreadFactory} instance to use.
   * @param factory the metrics
   * @return this builder instance
   */
  public VertxBuilder threadFactory(VertxThreadFactory factory) {
    this.threadFactory = factory;
    return this;
  }

  /**
   * @return the {@code ExecutorServiceFactory} to use
   */
  public ExecutorServiceFactory executorServiceFactory() {
    return executorServiceFactory;
  }

  /**
   * Set the {@code ExecutorServiceFactory} instance to use.
   * @param factory the factory
   * @return this builder instance
   */
  public VertxBuilder executorServiceFactory(ExecutorServiceFactory factory) {
    this.executorServiceFactory = factory;
    return this;
  }

  /**
   * Build and return the vertx instance
   */
  public Vertx vertx() {
    var vertx = new VertxImpl(
      options,
      null,
      null,
      transport,
      threadFactory,
      executorServiceFactory);
    vertx.init();
    return vertx;
  }

  /**
   * Initialize the service providers.
   * @return this builder instance
   */
  public VertxBuilder init() {
    initTransport();
    initFileResolver();
    Collection<VertxServiceProvider> providers = new ArrayList<>();
    initMetrics(options, providers);
    initTracing(options, providers);
    initClusterManager(options, providers);
    providers.addAll(ServiceHelper.loadFactories(VertxServiceProvider.class));
    initProviders(providers);
    initThreadFactory();
    initExecutorServiceFactory();
    return this;
  }

  private void initProviders(Collection<VertxServiceProvider> providers) {
    for (VertxServiceProvider provider : providers) {
      provider.init(this);
    }
  }

  private static void initTracing(VertxOptions options, Collection<VertxServiceProvider> providers) {
    TracingOptions tracingOptions = options.getTracingOptions();
    if (tracingOptions != null) {
      VertxTracerFactory factory = tracingOptions.getFactory();
      if (factory != null) {
        providers.add(factory);
      }
    }
  }

  private static void initClusterManager(VertxOptions options, Collection<VertxServiceProvider> providers) {
    ClusterManager clusterManager = options.getClusterManager();
    if (clusterManager == null) {
      String clusterManagerClassName = System.getProperty("vertx.cluster.managerClass");
      if (clusterManagerClassName != null) {
        // We allow specify a sys prop for the cluster manager factory which overrides ServiceLoader
        try {
          Class<?> clazz = Class.forName(clusterManagerClassName);
          clusterManager = (ClusterManager) clazz.newInstance();
        } catch (Exception e) {
          throw new IllegalStateException("Failed to instantiate " + clusterManagerClassName, e);
        }
      }
    }
    if (clusterManager != null) {
      providers.add(clusterManager);
    }
  }

  private void initTransport() {
    if (transport != null) {
      return;
    }
    transport = Transport.transport(options.getPreferNativeTransport());
  }

  private void initFileResolver() {
    if (fileResolver != null) {
      return;
    }
    fileResolver = new FileResolver(options.getFileSystemOptions());
  }

  private void initThreadFactory() {
    if (threadFactory != null) {
      return;
    }
    threadFactory = VertxThreadFactory.INSTANCE;
  }

  private void initExecutorServiceFactory() {
    if (executorServiceFactory != null) {
      return;
    }
    executorServiceFactory = ExecutorServiceFactory.INSTANCE;
  }
}
