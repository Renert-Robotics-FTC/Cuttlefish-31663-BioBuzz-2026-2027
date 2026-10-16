package org.firstinspires.ftc.teamcode.subsystems;

/** Decision logic with no FTC hardware dependencies, so it can be tested off the robot. */
public final class IntakeController {
    public enum Request { STOP, COLLECT, FEED, EJECT }
    public enum State { SENSOR_WARMUP, SENSOR_FAULT, STOPPED, COLLECTING, FULL, FEEDING, EJECTING }

    private final double[] presentThresholds;
    private final double[] absentThresholds;
    private final int ballCapacity;
    private final double debounceSeconds;
    // Track each sensor separately so flickering readings do not change the count.
    private final boolean[] ballPresent = new boolean[4];
    private final boolean[] pendingPresence = new boolean[4];
    private final boolean[] sensorReady = new boolean[4];
    private final double[] readingChangedAt = new double[4];
    private boolean started;
    private boolean full;
    private double lastTime = Double.NEGATIVE_INFINITY;
    private State state = State.STOPPED;

    public IntakeController(double[] on, double[] off, double debounceSeconds, int ballCapacity) {
        if (on == null || off == null || on.length != 4 || off.length != 4) {
            throw new IllegalArgumentException("Exactly four ON/OFF thresholds are required.");
        }
        if (ballCapacity < 1 || ballCapacity > on.length) {
            throw new IllegalArgumentException("Ball capacity must fit the sensor positions.");
        }
        this.ballCapacity = ballCapacity;
        for (int i = 0; i < 4; i++) {
            if (!Double.isFinite(on[i]) || !Double.isFinite(off[i])
                    || off[i] < 0 || on[i] <= off[i] || on[i] > 3) {
                throw new IllegalArgumentException("Calibrate sensor " + (i + 1) + ": 0 <= OFF < ON <= 3.");
            }
        }
        if (!Double.isFinite(debounceSeconds) || debounceSeconds < 0) {
            throw new IllegalArgumentException("Invalid intake timing settings.");
        }
        presentThresholds = on.clone();
        absentThresholds = off.clone();
        this.debounceSeconds = debounceSeconds;
    }

    /** Call once per TeleOp loop with brightness readings and monotonic time in seconds. */
    public void update(double[] brightness, double now, Request request) {
        if (request == null || !Double.isFinite(now) || now < lastTime) {
            throw new IllegalArgumentException("Use a valid request and a monotonic clock.");
        }
        lastTime = now;
        if (brightness == null || brightness.length != 4) {
            throw new IllegalArgumentException("Exactly four sensor readings are required.");
        }
        for (double value : brightness) {
            if (!Double.isFinite(value) || value < 0 || value > 3) {
                // Never keep running on an invalid reading. Require fresh stable samples to recover.
                resetSensorReadiness();
                state = State.SENSOR_FAULT;
                return;
            }
        }

        int rawBallCount = 0;
        for (int i = 0; i < 4; i++) {
            // Use a higher threshold to detect a ball and a lower one to clear it.
            boolean seesBall;
            if (pendingPresence[i]) {
                seesBall = brightness[i] > absentThresholds[i];
            } else {
                seesBall = brightness[i] >= presentThresholds[i];
            }

            // When a reading changes, restart the timer for that sensor.
            if (!started || seesBall != pendingPresence[i]) {
                pendingPresence[i] = seesBall;
                readingChangedAt[i] = now;
            }
            // Accept the new reading only after it stays steady long enough.
            if (now - readingChangedAt[i] >= debounceSeconds) {
                ballPresent[i] = pendingPresence[i];
                sensorReady[i] = true;
            }
            if (brightness[i] >= presentThresholds[i]) {
                rawBallCount++;
            }
        }
        started = true;

        // Clear automatically below capacity: no button release is required to refill.
        full = rawBallCount >= ballCapacity || (sensorsReady() && getBallCount() >= ballCapacity);

        if (!sensorsReady()) {
            state = State.SENSOR_WARMUP;
            return;
        }

        // Choose an action in priority order: stop, feed, reverse, then auto collect.
        if (request == Request.STOP) {
            if (full) {
                state = State.FULL;
            } else {
                state = State.STOPPED;
            }
        } else if (request == Request.FEED) {
            // Feeding must work even when full so stored balls can reach the shooter.
            state = State.FEEDING;
        } else if (request == Request.EJECT) {
            state = State.EJECTING;
        } else if (full) {
            state = State.FULL;
        } else {
            state = State.COLLECTING;
        }
    }

    public State getState() {
        return state;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isPresent(int slot) {
        return sensorReady[slot] && ballPresent[slot];
    }

    public boolean sensorsReady() {
        for (boolean ready : sensorReady) {
            if (!ready) {
                return false;
            }
        }
        return true;
    }

    public int getBallCount() {
        // -1 means unknown while sensors are starting up or recovering from an error.
        if (!sensorsReady()) {
            return -1;
        }
        int count = 0;
        for (boolean occupied : ballPresent) {
            if (occupied) {
                count++;
            }
        }
        return count;
    }
    public int motorDirection() {
        if (state == State.COLLECTING || state == State.FEEDING) {
            return 1;
        }
        if (state == State.EJECTING) {
            return -1;
        }
        return 0;
    }
    public static double rpmToTicksPerSecond(double rpm, double ticksPerRev) {
        if (!Double.isFinite(rpm) || !Double.isFinite(ticksPerRev) || ticksPerRev <= 0) {
            throw new IllegalArgumentException("Invalid motor speed conversion.");
        }
        return rpm * ticksPerRev / 60.0;
    }
    public void stop() {
        state = State.STOPPED;
    }
    private void resetSensorReadiness() {
        started = false;
        full = false;
        for (int i = 0; i < 4; i++) {
            sensorReady[i] = false;
            pendingPresence[i] = false;
            ballPresent[i] = false;
        }
    }
}
