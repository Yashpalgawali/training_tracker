package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Summary returned after a CI3 sync operation (on-demand or scheduled).
 */
@Getter
@AllArgsConstructor
public class Ci3SyncResult {

    /** Total records received from the CI3 API. */
    private final int totalFetched;

    /** Records saved as new employees. */
    private final int saved;

    /** Records skipped because the empCode already exists. */
    private final int skipped;

    /** Records that failed to save (bad data, lookup miss, etc.). */
    private final int failed;

    @Override
    public String toString() {
        return String.format(
            "Ci3SyncResult{totalFetched=%d, saved=%d, skipped=%d, failed=%d}",
            totalFetched, saved, skipped, failed);
    }
}
