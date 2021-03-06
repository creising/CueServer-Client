package org.urbanbyte.cueserver.http;

import com.google.common.annotations.VisibleForTesting;
import org.urbanbyte.cueserver.CueServerClient;
import org.urbanbyte.cueserver.data.cue.Cue;
import org.urbanbyte.cueserver.data.playback.CombineMode;
import org.urbanbyte.cueserver.data.playback.DetailedPlaybackStatus;
import org.urbanbyte.cueserver.data.playback.Playback;
import org.urbanbyte.cueserver.data.system.Model;
import org.urbanbyte.cueserver.data.playback.PlaybackInfo;
import org.urbanbyte.cueserver.data.playback.PlaybackStatus;
import org.urbanbyte.cueserver.data.system.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The {@code CueServerClient} facilitates interaction with a CueServer. Clients
 * have the ability to retrieve state related information, and send commands to
 * a CueServer using it HTTP interface.
 * <p>
 * author: Chris Reising
 */
public class HttpCueServerClient implements CueServerClient
{
    /** For logging. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(HttpCueServerClient.class);

    /** Expected size of the array returned when requesting system info. */
    private static final int SYSTEM_ARRAY_LEN = 78;

    /** Expected size of the array returned when requesting playback status. */
    private static final int PLAYBACK_STATUS_ARRAY_LEN = 48;

    /**
     * Expected size of the array returned when requesting detailed playback
     * status.
     */
    private static final int DETAILED_PLAYBACK_STATUS_ARRAY_LEN = 96;

    /** The URL used to execute commands. */
    private final String exeUrl;

    /** The URL used to get values. */
    private final String getUrl;

    /** The host and port of the CueServer the client is connected to. */
    private final String url;

    /** For submitting HTTP requests. */
    private final SimpleHttpClient httpClient;

    /**
     * Creates a new client with a default port of 80.
     *
     * @param host the host name or IP address of the CueServer.
     * @throws IllegalArgumentException if the host is {@code null}.
     */
    public HttpCueServerClient(String host)
    {
        this(host, 80);
    }

    /**
     * Creates a new client with the provided host and port.
     *
     * @param host the host name or IP address of the CueServer.
     * @param port the port of the web service. Must be within [0, 65535].
     * @throws IllegalArgumentException if the host is {@code null}, or if the
     * port is not valid.
     */
    public HttpCueServerClient(String host, int port)
    {
        this(host, port, new SimpleHttpClient());
    }

    /**
     * Creates a new client with the provided host port and client.
     *
     * @param host the host name or IP address of the CueServer.
     * @param port the port of the web service. Must be within [0, 65535].
     * @param httpClient the http client for the web service.
     * @throws IllegalArgumentException if the host or client is {@code null},
     * or if the port is not valid.
     */
    public HttpCueServerClient(String host,
                                int port,
                                SimpleHttpClient httpClient)
    {
        checkNotNull(host, "host cannot be null");
        checkArgument(port >= 0 && port <= 65535, "port is not valid");

        this.httpClient = checkNotNull(httpClient, "httpClient cannot be null");

        url = host + ":" + port;
        getUrl = url + "/get.cgi/?req=";
        exeUrl = url + "/exe.cgi/?cmd=";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemInfo getSystemInfo()
    {

        Integer[] byteArray = httpClient.submitHttpGetRequest(
                getUrl + "SI");

        SystemInfo info = null;
        if(byteArray == null || byteArray.length != SYSTEM_ARRAY_LEN)
        {
            LOGGER.warn("The array returned from the system is not the " +
                    "correct size.", SYSTEM_ARRAY_LEN);
        }
        else
        {
            SystemInfo.Builder builder = new SystemInfo.Builder();

            ParseStruct<String> parsedValue = bytesToString(
                    byteArray, 0, 16);
            builder.setSerialNumber(parsedValue.value);

            parsedValue = bytesToString(
                    byteArray, parsedValue.nextIndex, 24);
            builder.setDeviceName(parsedValue.value);

            parsedValue = bytesToString(
                    byteArray, parsedValue.nextIndex, 12);
            builder.setFirmwareVersion(parsedValue.value);

            parsedValue = bytesToString(
                    byteArray, parsedValue.nextIndex, 24);
            builder.setTime(parsedValue.value);

            builder.setModel(convertModel(
                    byteArray[parsedValue.nextIndex]));

            builder.setHasPassword(
                    !(byteArray[parsedValue.nextIndex + 1] == 0));
            info = builder.build();
        }

        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlaybackStatus getPlaybackStatus()
    {
        Integer[] byteArray = httpClient.submitHttpGetRequest(
                getUrl + "PS");

        PlaybackStatus status = null;
        if(byteArray == null ||  byteArray.length != PLAYBACK_STATUS_ARRAY_LEN)
        {
            LOGGER.warn("The array returned from the system is not the " +
                            "correct size. Expected {}",
                    PLAYBACK_STATUS_ARRAY_LEN);
        }
        else
        {
            PlaybackStatus.Builder builder =
                    new PlaybackStatus.Builder();

            PlaybackInfo pb = new PlaybackInfo.Builder()
                    .setPlayback(Playback.PLAYBACK_1)
                    .setCurrentCue(
                            parseCue(unsignedIntToInt(byteArray, 0)))
                    .setNextCue(
                            parseCue(unsignedIntToInt(byteArray, 2)))
                    .build();
            builder.setPlayback1(pb);

            pb = new PlaybackInfo.Builder()
                    .setPlayback(Playback.PLAYBACK_2)
                    .setCurrentCue(
                            parseCue(unsignedIntToInt(byteArray, 12)))
                    .setNextCue(
                            parseCue(unsignedIntToInt(byteArray, 14)))
                    .build();
            builder.setPlayback2(pb);

            pb = new PlaybackInfo.Builder()
                    .setPlayback(Playback.PLAYBACK_3)
                    .setCurrentCue(
                            parseCue(unsignedIntToInt(byteArray, 24)))
                    .setNextCue(
                            parseCue(unsignedIntToInt(byteArray, 26)))
                    .build();
            builder.setPlayback3(pb);

            pb = new PlaybackInfo.Builder()
                    .setPlayback(Playback.PLAYBACK_4)
                    .setCurrentCue(
                            parseCue(unsignedIntToInt(byteArray, 36)))
                    .setNextCue(
                            parseCue(unsignedIntToInt(byteArray, 38)))
                    .build();
            builder.setPlayback4(pb);

            status = builder.build();
        }

        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailedPlaybackStatus getDetailedPlaybackInfo(Playback playback)
    {
        Integer[] byteArray = httpClient.submitHttpGetRequest(
                getUrl + "PI&id=" + playback.getPlaybackId());

        DetailedPlaybackStatus status = null;
        if(byteArray == null ||
                byteArray.length != DETAILED_PLAYBACK_STATUS_ARRAY_LEN)
        {
            LOGGER.warn("The array returned from the system is not the " +
                            "correct size. Expected {}",
                    PLAYBACK_STATUS_ARRAY_LEN);
        }
        else
        {
            ParseStruct<String> currentName = bytesToString(byteArray, 32, 32);
            ParseStruct<String> nextName =
                    bytesToString(byteArray, currentName.nextIndex, 32);

            status = new DetailedPlaybackStatus.Builder()
                    .setPlayback(playback)
                    .setTimingDisabled(parseBoolean(byteArray[1]))
                    .setMasterLevel(byteArray[2])
                    .setCombinedMode(convertCombineMode(byteArray[3]))
                    .setCurrentCue(parseCue(unsignedIntToInt(byteArray, 12),
                            currentName.value))
                    .setNextCue(parseCue(unsignedIntToInt(byteArray, 14),
                            nextName.value))
                    .setLinkedCue(parseCue(unsignedIntToInt(byteArray, 22)))
                    .build();
        }
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer[] getOutputLevels()
    {
        Integer[] byteArray = httpClient.submitHttpGetRequest(getUrl + "OUT");

        Integer[] dmxValues  = null;
        if(byteArray == null || byteArray.length != 512)
        {
            LOGGER.warn("The values returned from the CueServer did not" +
                    "was not 512 bytes or null.");
        }
        else
        {
            dmxValues = byteArray;
        }
        return dmxValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playCue(double cueNumber)
    {
        playCue(cueNumber, Playback.PLAYBACK_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playCue(double cueNumber, Playback playback)
    {
        if(cueNumber <= 0)
        {
            LOGGER.error("cueNumber must be positive.");
            throw new IllegalArgumentException("cueNumber must be positive");
        }

        String cmd =
                "P+" + playback.getPlaybackId() + "+Q+" +
                        truncateValue(cueNumber) + "+GO";
        LOGGER.debug("Cue command: {}", cmd);

        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPlayback(Playback playback)
    {
        String cmd = "P+" + playback.getPlaybackId() + "+CL";
        LOGGER.debug("Cue command: {}", cmd);

        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannel(int channel, int value)
    {
        setChannel(channel, value, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannel(int channel, int value, double timeSeconds)
    {
        setChannel(channel, value, timeSeconds, Playback.PLAYBACK_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannel(int channel,
                           int value,
                           double timeSeconds,
                           Playback playback)
    {
        checkChannel(channel);
        checkChannelLevel(value);
        checkTime(timeSeconds);
        //ex: T 3 P1 C 1 A 50
        String cmd = "T+" + truncateValue(timeSeconds) + "+P" +
                playback.getPlaybackId() + "+C+" + channel + "+A+%23" + value;

        String fullUrl = exeUrl + cmd;
        LOGGER.info("Channel command: {}", fullUrl);
        httpClient.submitHttpGetRequest(fullUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannelRange(int startChannel, int endChannel, int value)
    {
        setChannelRange(startChannel, endChannel, value, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannelRange(int startChannel, int endChannel, int value,
                                double timeSeconds)
    {
        setChannelRange(startChannel, endChannel, value, timeSeconds,
                Playback.PLAYBACK_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChannelRange(int startChannel, int endChannel, int value,
                                double timeSeconds, Playback playback)
    {
        checkChannel(startChannel);
        checkChannel(endChannel);
        if(endChannel < startChannel)
        {
            LOGGER.error("end channel cannot be < start channel");
            throw new IllegalArgumentException("end channel cannot be < " +
                    "than the start channel");
        }
        checkChannelLevel(value);
        checkTime(timeSeconds);

        String cmd = "T+" + truncateValue(timeSeconds) + "+P" +
                playback.getPlaybackId() + "+C+" + startChannel +
                "%3E" + endChannel + "+A%23" + value;
        LOGGER.debug("Range command: {}", cmd);
        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordCue(double cueNumber,
                          double uptimeSecs,
                          double downtimeSecs)
    {
        checkCueNumber(cueNumber);
        checkTime(uptimeSecs);
        checkTime(downtimeSecs);

        String cmd = "FA+" + truncateValue(uptimeSecs) + "%2F" +
                truncateValue(downtimeSecs) + "%3B" + "RQ+" +
                truncateValue(cueNumber);
        LOGGER.debug("Rec cue command: {}", cmd);
        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCue(double cueNumber)
    {
        checkCueNumber(cueNumber);
        String cmd = "DELQ+" + truncateValue(cueNumber);
        LOGGER.debug("Delete command: {}", cmd);
        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCue(double cueNumber)
    {
        checkCueNumber(cueNumber);
        String cmd = "UQ+" + truncateValue(cueNumber);
        LOGGER.debug("Delete command: {}", cmd);
        httpClient.submitHttpGetRequest(exeUrl + cmd);
    }

    /**
     * Checks to make the provided cue number is valid.
     *
     * @param cueNumber the channel to check.
     * @throws IllegalArgumentException if the number is not valid.
     */
    private void checkCueNumber(double cueNumber)
    {
        if(cueNumber <= 0)
        {
            LOGGER.error("cueNumber must be positive.");
            throw new IllegalArgumentException("cueNumber must be positive");
        }
    }

    /**
     * Checks to make the provided DMX channel is valid.
     *
     * @param channel the channel to check.
     * @throws IllegalArgumentException if the channel is not valid.
     */
    private void checkChannel(int channel)
    {
        if(channel < 1 || channel > 512)
        {
            LOGGER.error("channel must be within [1, 512]. Given: {}", channel);
            throw new IllegalArgumentException(
                    "channel must be within [1, 512]");
        }
    }

    /**
     * Checks to make the provided DMX channel level is valid.
     *
     * @param value the value to check.
     * @throws IllegalArgumentException if the value is not valid.
     */
    private void checkChannelLevel(int value)
    {
        if(value < 0 || value > 255)
        {
            LOGGER.error("value must be within [0, 255]. Given: {}", value);
            throw new IllegalArgumentException(
                    "value must be within [0, 255].");
        }
    }

    /**
     * Creates a string from the given double. The CueServer only supports up
     * to one decimal place of precision. Any other reaming values will be
     * truncated.
     *
     * @param time the time to format.
     * @return Never {@code null}.
     */
    private String truncateValue(double time)
    {
        return String.format("%.1f", time);
    }

    /**
     * Checked to make the time is valid.
     *
     * @param timeSeconds the time to check.
     * @throws IllegalArgumentException if the time is not valid.
     */
    private void checkTime(double timeSeconds)
    {
        if(timeSeconds < 0 || timeSeconds > 65000)
        {
            LOGGER.error("time must be >= 0 and <= 65000. Given {}.",
                    timeSeconds);
            throw new IllegalArgumentException("time must be positive.");
        }
    }

    /**
     * Gets the URL of the CueServer.
     * @return Never {@code null}.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Converts the given integer value to {@code Model}.
     *
     * @param modelValue the model to convert.
     * @return the model.
     */
    @VisibleForTesting
    protected Model convertModel(int modelValue)
    {
        Model model;
        switch (modelValue)
        {
            case 1:
                model = Model.CS_800;
                break;
            case 2:
                model = Model.CS_810;
                break;
            case 3:
                model = Model.CS_816;
                break;
            case 4:
                model = Model.CS_840;
                break;
            default:
                model = Model.UNKNOWN;
                break;

        }
        return model;
    }

    /**
     * Converts the given value to a {@code CombineMode}.
     *
     * @param value the value to convert.
     * @return Never {@code null}.
     */
    @VisibleForTesting
    protected static CombineMode convertCombineMode(int value)
    {
        CombineMode combineMode;
        switch (value)
        {
            case 0:
                combineMode = CombineMode.MERGE;
                break;
            case 1:
                combineMode = CombineMode.OVERRIDE;
                break;
            case 2:
                combineMode = CombineMode.SCALE;
                break;
            default:
                LOGGER.warn("Unknown combine mode {}", value);
                combineMode = CombineMode.UNKNOWN;
                break;
        }
        return combineMode;
    }

    /**
     * Parses the given integer value into a decimal cue number.
     *
     * @param rawNumber 16 bit number from the CueServer.
     * @return {@code null} if the given number is 0 or 65535, otherwise the
     *         number.
     */
    @VisibleForTesting
    protected static Cue parseCue(int rawNumber)
    {
        return parseCue(rawNumber, null);
    }

    /**
     * Creates a {@link Cue} from the given number and name.
     *
     * @param rawNumber the cue number from the CueServer.
     * @param name the name, or {@code null} if the name is not known.
     * @return the {@link Cue} or {@code null} if the cue number is
     *         65535 or 0.
     */
    @VisibleForTesting
    protected static Cue parseCue(int rawNumber, String name)
    {
        Cue cueNumber = null;
        if(rawNumber != 65535 && rawNumber != 0)
        {
            cueNumber = new Cue(rawNumber / 10d, name);
        }
        return cueNumber;
    }

    /**
     * Parses the given integer into a boolean.
     *
     * @param value the value to parse.
     * @return {@code true} if {@code value} not 0, {@code false} if
     *         {@code value} is 0.
     */
    private static boolean parseBoolean(int value)
    {
        return value != 0;
    }

    /**
     * Converts a subset of character values represented in integers
     * into a {@code String} of ASCII characters.
     *
     * @param byteArray the array to parse from.
     * @param startIndex the index of the array to start from.
     * @param size the number of indices that should be parsed.
     * @return a struct containing the result as well as the next index to
     *         start parsing from.
     * @throws ArrayIndexOutOfBoundsException if the indices being checked are
     *                                        not valid..
     */
    @VisibleForTesting
    protected static ParseStruct<String> bytesToString(Integer[] byteArray,
                                                     int startIndex,
                                                     int size)
    {
        // find the last (exclusive) index to start from.
        int nextAvailIndex = startIndex + size;
        checkIndex(byteArray, startIndex, nextAvailIndex);

        StringBuilder builder = new StringBuilder();
        int index = startIndex;
        for(; index < nextAvailIndex ; index++)
        {
            Integer value = byteArray[index];

            if(value != null && value != 0)
            {
                char c = (char)byteArray[index].intValue();
                builder.append(c);
            }
        }

        ParseStruct<String> parseStruct = new ParseStruct<String>();
        parseStruct.value = builder.toString();
        parseStruct.nextIndex = nextAvailIndex;
        return parseStruct;
    }

    /**
     * Converts the byte value represented as an integer found in
     * {@code startIndex} and {@code startIndex} plus 1 into its equivalent 16
     * bit integer value.
     *
     * @param array the array of values.
     * @param startIndex the starting index.
     * @return the converted value.
     * @throws ArrayIndexOutOfBoundsException if {@code startIndex} or
     *                                        {@code startIndex} plus one is
     *                                        outside the bounds of
     *                                        {@code array}.
     */
    @VisibleForTesting
    protected static int unsignedIntToInt(Integer[] array, int startIndex)
    {
        return ((array[startIndex + 1]) <<8 ) | (array[startIndex] & 0xFF);
    }

    /**
     * Helper method to check to see if the given indices are valid with
     * respect to the provided array.
     *
     * @param array the array to validate against.
     * @param startIndex the lower bounds of the range. Must be &le; to the
     *                   end index. Cannot be &lt; zero, or &gt; the length
     *                   of {@code array}.
     * @param endIndex the upper bounds of the range. Cannot be &lt; zero, or
     *                 &gt; the length of {@code array}.
     * @throws ArrayIndexOutOfBoundsException if the indices being checked are
     *                                        not valid.
     */
    @VisibleForTesting
    protected static void checkIndex(Integer[] array,
                                   int startIndex,
                                   int endIndex)
    {
        boolean isValid;
        int arrayLength = array.length;
        String errorMessage = null;

        if(startIndex > endIndex)
        {
            errorMessage = "The start index cannot be greater than the end" +
                    "index.";
            isValid = false;
        }
        else if(startIndex < 0 || startIndex >= arrayLength)
        {
             errorMessage = "The start index cannot be < 0, or >= array length" +
                     "{}.";
            isValid = false;
        }
        else if(endIndex > arrayLength)
        {
            errorMessage = "The end index cannot >= array length.";
            isValid = false;
        }
        else
        {
            LOGGER.debug("The array indices are valid.");
            isValid = true;
        }

        if(!isValid)
        {
            LOGGER.error("The array indices are not valid {} {} {}.",
                    startIndex, endIndex, arrayLength);
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }
    }

    /**
     * Struct used for returning a parsed value from a byte array and the
     * next index the parser should start from.
     * @param <T> the type being parsed.
     */
    @VisibleForTesting
    protected static final class ParseStruct<T>
    {
        /** The parsed value. */
        protected T value;
        /** The next index. */
        protected int nextIndex;
    }
}
