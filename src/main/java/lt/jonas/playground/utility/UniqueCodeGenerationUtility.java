package lt.jonas.playground.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniqueCodeGenerationUtility {
    public static String generateUniqueCode() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString();
    }
}
