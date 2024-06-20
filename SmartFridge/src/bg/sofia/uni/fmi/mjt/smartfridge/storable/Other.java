package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;

public class Other extends StorableImpl {

    public Other(String name, LocalDate expirationDate) {
        super(name, expirationDate);
    }

    @Override
    public StorableType getType() {
        return StorableType.OTHER;
    }
}
