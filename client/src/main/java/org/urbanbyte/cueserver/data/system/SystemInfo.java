package org.urbanbyte.cueserver.data.system;


import static com.google.common.base.Preconditions.*;

/**
 * Contains details of a CueServer.
 * <p>
 * author: Chris Reising
 */
public class SystemInfo
{
    /** The name of the CueServer. */
    private final String deviceName;

    /** The model of the CueServer. */
    private final Model model;

    /** The serial number. */
    private final String serialNumber;

    /** The version of firmware running on the CueServer. */
    private final String firmwareVersion;

    /** The time of the CueServer. */
    private final String time;

    /** Whether or not the CueServer has a password. */
    private final boolean hasPassword;

    /**
     * Creates a new {@code SystemInfo} with the given builder.
     *
     * @param builder the builder.
     * @throws NullPointerException if any parameter in the builder is
     * {@code null}.
     */
    private SystemInfo(Builder builder)
    {
        deviceName = checkNotNull(builder.getDeviceName(),
                "deviceName cannot be null");
        model = checkNotNull(builder.getModel(), "model cannot be null");
        serialNumber = checkNotNull(builder.getSerialNumber(),
                "serialNumber cannot be null");
        firmwareVersion = checkNotNull(builder.getFirmwareVersion(),
                "firmware cannot be null");
        time = checkNotNull(builder.getTime(), "time cannot be null");
        hasPassword = builder.hasPassword();
    }

    /**
     * Gets the name of the device.
     *
     * @return Never {@code null}.
     */
    public String getDeviceName()
    {
        return deviceName;
    }

    /**
     * Get the model.
     *
     * @return Never {@code null}.
     */
    public Model getModel()
    {
        return model;
    }

    /**
     * Gets the serial number.
     *
     * @return Never {@code null}.
     */
    public String getSerialNumber()
    {
        return serialNumber;
    }

    /**
     * Gets the firmware version.
     *
     * @return Never {@code null}.
     */
    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    /**
     * Gets the time from the device at the time of the poll.
     *
     * @return Never {@code null}.
     */
    public String getTime()
    {
        return time;
    }

    /**
     * {@code true} if the device uses a password, {@code false} if not.
     *
     * @return Never {@code null}.
     */
    public boolean hasPassword()
    {
        return hasPassword;
    }

    @Override
    public String toString()
    {
        return "SystemInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", model=" + model +
                ", serialNumber='" + serialNumber + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", time='" + time + '\'' +
                ", hasPassword=" + hasPassword +
                '}';
    }

    /**
     * Builds a new {@link SystemInfo}.
     */
    public static class Builder
    {
        /** The value used for the device name. */
        private String deviceName;

        /** The value used for the model. */
        private Model model;

        /** The value used for the serial number. */
        private String serialNumber;

        /** The value used for the firmware version. */
        private String firmwareVersion;

        /** The value used for the time. */
        private String time;

        /** The value used for the password. */
        private Boolean hasPassword;

        /**
         * Gets the value used for the device name.
         *
         * @return Can be {@code null}.
         */
        public String getDeviceName()
        {
            return deviceName;
        }

        /**
         * Sets the device name.
         *
         * @param deviceName the name to set.
         * @return {@code this} builder.
         */
        public Builder setDeviceName(String deviceName)
        {
            this.deviceName = deviceName;
            return this;
        }

        /**
         * Gets the model.
         *
         * @return Can be {@code null}.
         */
        public Model getModel()
        {
            return model;
        }

        /**
         * Sets the model.
         *
         * @param model the model to set.
         * @return {@code this} builder.
         */
        public Builder setModel(Model model)
        {
            this.model = model;
            return this;
        }

        /**
         * Gets the serial number.
         *
         * @return Can be {@code null}.
         */
        public String getSerialNumber()
        {
            return serialNumber;
        }

        /**
         * Sets the serial number.
         *
         * @param serialNumber the serial number.
         * @return {@code this} builder.
         */
        public Builder setSerialNumber(String serialNumber)
        {
            this.serialNumber = serialNumber;
            return this;
        }

        /**
         * Gets the firmware.
         *
         * @return Can be {@code null}.
         */
        public String getFirmwareVersion()
        {
            return firmwareVersion;
        }

        /**
         * Sets the firmware version.
         *
         * @param firmwareVersion the firmware version.
         * @return {@code this} builder.
         */
        public Builder setFirmwareVersion(String firmwareVersion)
        {
            this.firmwareVersion = firmwareVersion;
            return this;
        }

        /**
         * Gets the time.
         *
         * @return Can be {@code null}.
         */
        public String getTime()
        {
            return time;
        }

        /**
         * Sets the time.
         *
         * @param time the time
         * @return {@code this} builder.
         */
        public Builder setTime(String time)
        {
            this.time = time;
            return this;
        }

        /**
         * Gets the password state.
         *
         * @return Can be {@code null}.
         */
        public Boolean hasPassword()
        {
            return hasPassword;
        }

        /**
         * Sets the password state.
         *
         * @param hasPassword the state of the password.
         * @return {@code this} builder.
         */
        public Builder setHasPassword(Boolean hasPassword)
        {
            this.hasPassword = hasPassword;
            return this;
        }

        /**
         * Builds a new {@link SystemInfo} using the values set in the builder.
         *
         * @return Never {@code null}.
         * @throws NullPointerException if any value in the builder is
         *                              {@code null}.
         */
        public SystemInfo build()
        {
            return new SystemInfo(this);
        }
    }
}
