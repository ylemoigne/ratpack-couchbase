package fr.javatic.ratpack.couchbase.couchbase;

import java.util.Objects;

class DefaultBucketInfo implements BucketInfo {
    private final String name;
    private final String password;

    public DefaultBucketInfo(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!obj.getClass().isAssignableFrom(BucketInfo.class)) {
            return false;
        }

        BucketInfo that = (BucketInfo) obj;
        return Objects.equals(this.name, that.getName());
    }
}