package io.github.rosestack.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.f4b6a3.uuid.UuidCreator;

class UuidsTest {
    private static final Logger log = LoggerFactory.getLogger(UuidsTest.class);

    @Test
    void testGetTimeOrderedEpoch() {
        UUID uuid = UuidCreator.getTimeOrderedEpoch();
        log.info("UUIDv7: {}", uuid);
        assertNotNull(uuid);
    }

    @Test
    void testUuidToBase64AndBack() {
        UUID uuid = UuidCreator.getTimeOrderedEpoch();
        // Encode for URL
        String urlSafeId = Uuids.uuidToBase64(uuid);
        log.info("URL Safe ID:   {}", urlSafeId);

        // Decode from URL
        UUID decodedUuid = Uuids.base64ToUuid(urlSafeId);
        log.info("Decoded UUID:  {}", decodedUuid);

        log.info("Match: {}", uuid.equals(decodedUuid));
        assertEquals(uuid, decodedUuid);
    }

    @Test
    void testFastRandomUUID() {
        String simpleUUID = Uuids.fastRandomUUID();
        log.info("Fast Simple UUID: {}", simpleUUID);
        assertNotNull(simpleUUID);
        assertTrue(simpleUUID.length() > 0);
    }
}
