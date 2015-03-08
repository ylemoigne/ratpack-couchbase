ratpack-couchbase
====

Import in Build
---------------
Using Gradle

    repositories {
        maven {
            url "http://dl.bintray.com/ylemoigne/maven"
        }
    }

    dependencies {
        compile 'fr.javatic.ratpack:ratpack-couchbase:0.1'
    }

Initialize
----------

    bindingsSpec.add(CouchbaseModule.class, config -> {
        config.hosts("localhost");
        config.administrator("admin", "password");
        config.bucketsSettings(
            DefaultBucketSettings.builder()
                .name("users")
                .type(BucketType.COUCHBASE)
                .quota(128)
                .build()
        );
    });

`hosts` (mandatory) couchbase cluster hosts.

`bucketSettings` (optional) create or update buckets according to their settings.

`administrator` (mandatory if bucketSettings) administrator credential.

Usage Sample
------------

    context.get(CouchbaseService.class)
        .getAsyncBucket(BucketInfo.create("users))
        .get(email, RawJsonDocument.class)
        .map(this::toUser)
        .subscribe(context::render)