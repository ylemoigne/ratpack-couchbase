package fr.javatic.ratpack.couchbase.couchbase;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class CouchbaseService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CouchbaseService.class);

    private final ConcurrentHashMap<BucketInfo, Bucket> buckets;

    private CouchbaseCluster cluster;

    public CouchbaseService(CouchbaseCluster cluster) {
        this.cluster = cluster;
        this.buckets = new ConcurrentHashMap<>();
    }

    public Bucket getBucket(BucketInfo bucketInfo) {
        return this.buckets.computeIfAbsent(bucketInfo, b -> {
            LOGGER.debug("Opening bucket {}", b.getName());
            return this.cluster.openBucket(b.getName(), b.getPassword());
        });
    }

    public AsyncBucket getAsyncBucket(BucketInfo bucketInfo) {
        return getBucket(bucketInfo).async();
    }
}
