package com.dmarcini.app.utils;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class Timer implements Serializable {
    private Instant start;

    public Timer() {
        this.start = Instant.now();
    }

    public long elapsed() {
        return Duration.between(start, Instant.now()).toSeconds();
    }

    public void reset() {
        start = Instant.now();
    }
}
