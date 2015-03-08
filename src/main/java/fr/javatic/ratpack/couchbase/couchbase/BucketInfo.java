package fr.javatic.ratpack.couchbase.couchbase;

import com.couchbase.client.java.cluster.BucketSettings;

public interface BucketInfo {
    default BucketInfo create(String name) {
        return new DefaultBucketInfo(name, null);
    }

    default BucketInfo create(String name, String password) {
        return new DefaultBucketInfo(name, null);
    }

    default BucketInfo create(BucketSettings settings) {
        return new DefaultBucketInfo(settings.name(), settings.password());
    }

    String getName();

    String getPassword();
}
