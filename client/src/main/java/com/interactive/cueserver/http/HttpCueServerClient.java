package com.interactive.cueserver.http;

import com.google.common.annotations.VisibleForTesting;
import com.interactive.cueserver.CueServerClient;
import com.interactive.cueserver.data.cue.Cue;
import com.interactive.cueserver.data.playback.Playback;
import com.interactive.cueserver.data.system.Model;
import com.interactive.cueserver.data.playback.PlaybackInfo;
import com.interactive.cueserver.data.playback.PlaybackStatus;
import com.interactive.cueserver.data.system.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Communicates with a  CueServer over HTTP to request real-time information
 * about the show it is playing back, as well as send live data to CueServer
 * that it should output to the connected devices.
 *
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemInfo getSystemInfo()
    {

        Integer[] byteArray = httpClient.submitHttpGetRequest(
                url + "/get.cgi/?req=SI");

        SystemInfo info = null;
        if(byteArray == null || byteArray.length != SYSTEM_ARRAY_LEN)
        {
            LOGGER.warn("The array returned from the system is not the " +
                    "correct size.", SYSTEM_ARRAY_LEN);
        }
        else
        {
            SystemInfo.SystemBuilder builder = new SystemInfo.SystemBuilder();

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
                url + "/get.cgi/?req=PS");

        PlaybackStatus status = null;
        if(byteArray == null ||  byteArray.length != PLAYBACK_STATUS_ARRAY_LEN)
        {
            LOGGER.warn("The array returned from the system is not the " +
                            "correct size. Expected {}",
                    PLAYBACK_STATUS_ARRAY_LEN);
        }
        else
        {
            PlaybackStatus.PlaybackStatusBuilder builder =
                    new PlaybackStatus.PlaybackStatusBuilder();

            PlaybackInfo pb = new PlaybackInfo(Playback.PLAYBACK_1,
                    parseCueNumber(unsignedIntToInt(byteArray, 0)),
                    parseCueNumber(unsignedIntToInt(byteArray, 2)));
            builder.setPlayback1(pb);

            pb = new PlaybackInfo(Playback.PLAYBACK_2,
                    parseCueNumber(unsignedIntToInt(byteArray, 12)),
                    parseCueNumber(unsignedIntToInt(byteArray, 14)));
            builder.setPlayback2(pb);

            pb = new PlaybackInfo(Playback.PLAYBACK_3,
                    parseCueNumber(unsignedIntToInt(byteArray, 24)),
                    parseCueNumber(unsignedIntToInt(byteArray, 26)));
            builder.setPlayback3(pb);

            pb = new PlaybackInfo(Playback.PLAYBACK_4,
                    parseCueNumber(unsignedIntToInt(byteArray, 36)),
                    parseCueNumber(unsignedIntToInt(byteArray, 38)));
            builder.setPlayback4(pb);

            status = builder.build();
        }

        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer[] getOutputLevels()
    {
        Integer[] byteArray = httpClient.submitHttpGetRequest(
                url + "/get.cgi/?req=OUT");

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
     * Gets the URL of the CueServer.
     * @return Never {@code null}.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Converts the given integer value to an enum.
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
     * Parses the given integer value into a decimal cue number.
     *
     * @param rawNumber 16 bit number from the CueServer.
     * @return {@code null} if the given number is 0 or 65535, otherwise the
     *         number.
     */
    @VisibleForTesting
    protected Cue parseCueNumber(int rawNumber)
    {
        Cue cueNumber = null;
        if(rawNumber != 65535 && rawNumber != 0)
        {
            cueNumber = new Cue(rawNumber / 10d);
        }
        return cueNumber;
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
     * @throws ArrayIndexOutOfBoundsException {@see checkIndex}.
     */
    private static ParseStruct<String> bytesToString(Integer[] byteArray,
                                                     int startIndex,
                                                     int size)
    {
        // find the last (exclusive) index to start from.
        int endIndex = startIndex + size;
        checkIndex(byteArray, startIndex, endIndex);

        StringBuilder builder = new StringBuilder();
        int index = startIndex;
        for(; index < endIndex ; index++)
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
        parseStruct.nextIndex = index;
        return parseStruct;
    }

    /**
     * Converts the byte value found in {@code startIndex} and
     * {@code startIndex} plus 1 into its equivalent 16 bit unsigned integer
     * value.
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
    protected int unsignedIntToInt(Integer[] array, int startIndex)
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
        else if(endIndex >= arrayLength)
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
            LOGGER.debug("The array indices are not valid {} {} {}.",
                    startIndex, endIndex, arrayLength);
            throw new ArrayIndexOutOfBoundsException(errorMessage);
        }
    }

    /**
     * Struct used for returning a parsed value from a byte array and the
     * next index the parser should start from.
     * @param <T> the type being parsed.
     */
    private static final class ParseStruct<T>
    {
        /** The parsed value. */
        private T value;
        /** The next index. */
        private int nextIndex;
    }
}
