package com.interactive.cueserver;

import com.google.common.annotations.VisibleForTesting;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides a simple interface for submitting HTTP get requests.
 *
 * author: Chris Reising
 */
public class SimpleHttpClient
{
    /** For logging. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SimpleHttpClient.class);

    /** Client used to submit requests. */
    private final CloseableHttpClient httpClient;

    /**
     * Creates a new {@code HttpClientWrapper}.
     */
    public SimpleHttpClient()
    {
        this(HttpClients.createDefault());
    }

    /**
     * Creates a new {@code HttpClientWrapper} with the provided
     * {@link org.apache.http.impl.client.CloseableHttpClient}.
     * @throws NullPointerException if {@code httpClient} is {@code null}.
     */
    public SimpleHttpClient(CloseableHttpClient httpClient)
    {
        this.httpClient = checkNotNull(httpClient, "httpClient cannot be null");
    }

    /**
     * Submits the provided URL as a HTTP get request.
     *
     * @param fullUrl the URL to submit.
     * @return the bytes read from the request as integers with value [0, 255];
     * @throws NullPointerException if {@code fullUrl} is {@code null}.
     */
    public Integer[] submitHttpGetRequest(String fullUrl)
    {
        checkNotNull(fullUrl, "fullUrl cannot be null");
        HttpGet get = new HttpGet(fullUrl);
        CloseableHttpResponse response = null;
        Integer[] readBytes = null;

        try
        {
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                readBytes = packBytes(entity.getContent());
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Error while communicating with the server.", e);
        }
        finally
        {
            closeResponse(response);
        }
        return readBytes;
    }

    /**
     * Helper methods that reads the bytes from the given stream and creates
     * an array of integers.
     * @param stream the stream to read. The stream will be closed before this
     *               method returns.
     * @return the bytes read from the request as integers with value [0, 255].
     *         If there was an error while reading the stream, {@code null}
     *         will be returned.
     */
    @VisibleForTesting
    protected Integer[] packBytes(InputStream stream)
    {
        Integer[] byteArray = null;
        try
        {
            List<Integer> bytes = new LinkedList<Integer>();
            int currentByte = stream.read();

            while (currentByte != -1)
            {
                bytes.add(currentByte);
                currentByte = stream.read();
            }
            byteArray= bytes.toArray(new Integer[bytes.size()]);
        }
        catch (IOException ioe)
        {
            LOGGER.error("Error while reading byte stream", ioe);
        }
        finally
        {
            closeStream(stream);
        }
        return byteArray;
    }

    /**
     * Helper to isolate the logic needed to close a stream to help keep other
     * sections easier to read.
     *
     * @param stream the stream to close.
     */
    private void closeStream(InputStream stream)
    {
        if(stream != null)
        {
            try
            {
                stream.close();
            }
            catch (IOException e)
            {
                LOGGER.warn("Error while closing steam", e);
            }
        }
    }

    /**
     * Helper to isolate the logic needed to close a response to help keep other
     * sections easier to read.
     *
     * @param response the response to close.
     */
    private void closeResponse(CloseableHttpResponse response)
    {
        if(response != null)
        {
            try
            {
                response.close();
            }
            catch (IOException e)
            {
                LOGGER.warn("Error while trying to close the response.");
            }
        }
    }
}
