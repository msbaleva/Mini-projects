package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.Objects;

public abstract class StorableImpl implements Storable {


    private String name;
    private LocalDate expiration;

    public StorableImpl(String name, LocalDate expirationDate) {
        this.name = name;
        this.expiration = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorableImpl storable = (StorableImpl) o;
        return Objects.equals(name, storable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDate getExpiration() {
        return expiration;
    }

    @Override
    public boolean isExpired() {
        return expiration.isBefore(LocalDate.now());
    }
}
