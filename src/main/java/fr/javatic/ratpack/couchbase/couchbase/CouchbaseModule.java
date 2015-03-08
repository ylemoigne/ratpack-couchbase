package fr.javatic.ratpack.couchbase.couchbase;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.guice.ConfigurableModule;

import java.util.Arrays;
import java.util.List;

public class CouchbaseModule extends ConfigurableModule<CouchbaseModule.Config> {
    private final static Logger LOGGER = LoggerFactory.getLogger(CouchbaseModule.class);

    @Override
    protected void configure() {
    }

    public static class Config {
        private List<String> hosts;
        private List<BucketSettings> bucketsSettings;
        private String adminUsername;
        private String adminPassword;

        public Config hosts(String... hosts) {
            this.hosts = Arrays.asList(hosts);
            return this;
        }

        public List<String> getHosts() {
            return hosts;
        }

        public Config administrator(String adminUsername, String adminPassword) {
            this.adminUsername = adminUsername;
            this.adminPassword = adminPassword;
            return this;
        }

        public Config bucketsSettings(BucketSettings... buckets) {
            this.bucketsSettings = Arrays.asList(buckets);
            return this;
        }

        public String getAdminUsername() {
            return adminUsername;
        }

        public String getAdminPassword() {
            return adminPassword;
        }

        public List<BucketSettings> getBucketsSettings() {
            return bucketsSettings;
        }

    }

    @Provides
    @Singleton
    protected CouchbaseCluster couchbaseCluster(Config config) {
        LOGGER.info("Creating CouchbaseCluster");
        final CouchbaseCluster cluster = CouchbaseCluster.create(
            DefaultCouchbaseEnvironment.builder().build(),
            config.getHosts()
        );

        // The ratpack Service.stop() api isn't called on JVM exit, and it's very important to free the cluster, so...
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Disconnecting CouchbaseCluster");
            cluster.disconnect();
        }));

        initBuckets(config, cluster);

        return cluster;
    }

    private void initBuckets(Config config, CouchbaseCluster cluster) {
        ClusterManager manager = cluster.clusterManager(config.getAdminUsername(), config.getAdminPassword());
        config.getBucketsSettings().forEach(bucket -> {
            if (!manager.hasBucket(bucket.name())) {
                manager.insertBucket(bucket);
            } else {
                manager.updateBucket(bucket);
            }
        });
    }


    @Provides
    @Singleton
    protected CouchbaseService couchbaseService(CouchbaseCluster cluster) {
        return new CouchbaseService(cluster);
    }
}
