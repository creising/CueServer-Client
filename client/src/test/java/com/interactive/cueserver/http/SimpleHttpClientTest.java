package com.interactive.cueserver.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests the {@code HttpClientWrapper} class.
 *
 * author: Chris Reising
 */
public class SimpleHttpClientTest
{
    /** Mocked client. */
    private CloseableHttpClient mockedClient;

    /** Mocked HTTP response. */
    private CloseableHttpResponse mockedResponse;

    /** Mocked input stream for returning the get request. */
    private InputStream mockedStream;

    /** Wrapper being tested. */
    private SimpleHttpClient wrapper;

    /** Mocked HTTP entity. */
    private HttpEntity mockedEntity;

    /**
     * Reset mocked objects between tests.
     * @throws IOException will not occur since the objects are mocked.
     */
    @Before
    public void setupTests() throws IOException
    {
        mockedClient = mock(CloseableHttpClient.class);
        mockedStream = mock(InputStream.class);
        mockedResponse = mock(CloseableHttpResponse.class);
        mockedEntity = mock(HttpEntity.class);

        wrapper = new SimpleHttpClient(mockedClient);

    }

    /**
     * Creating a wrapper with a {@code null} HTTP client will cause an
     * exception.
     */
    @Test(expected = NullPointerException.class)
    public void nullClientException()
    {
        new SimpleHttpClient(null);
    }

    /**
     * Successfully submit a request and check the response from the stream.
     * @throws IOException will not occur since the objects are mocked.
     */
    @Test
    public void submitRequest() throws IOException
    {
        String testUrl = "url";
        ArgumentCaptor<HttpGet> getCaptor =
                ArgumentCaptor.forClass(HttpGet.class);

        when(mockedClient.execute(any(HttpGet.class))).thenReturn(
                mockedResponse);
        when(mockedResponse.getEntity()).thenReturn(mockedEntity);
        when(mockedEntity.getContent()).thenReturn(mockedStream);
        when(mockedStream.read()).thenReturn(1).thenReturn(2).thenReturn(-1);

        Integer[] result = wrapper.submitHttpGetRequest(testUrl);

        assertThat(result.length, is(2));
        assertThat(result[0], is(1));
        assertThat(result[1], is(2));

        verify(mockedClient).execute(getCaptor.capture());
        verify(mockedResponse).close();
        verify(mockedStream).close();
    }

    /**
     * If the stream throws an exception while being closed the exception is
     * handled by the client.
     */
    @Test
    public void submitRequestStreamThrowsExceptionOnClose() throws IOException
    {
        String testUrl = "url";
        ArgumentCaptor<HttpGet> getCaptor =
                ArgumentCaptor.forClass(HttpGet.class);

        when(mockedClient.execute(any(HttpGet.class))).thenReturn(
                mockedResponse);
        when(mockedResponse.getEntity()).thenReturn(mockedEntity);
        when(mockedEntity.getContent()).thenReturn(mockedStream);
        when(mockedStream.read()).thenReturn(1).thenReturn(2).thenReturn(-1);

        // if we throw an exception while trying to close we should be able
        // to recover
        doThrow(new IOException()).when(mockedStream).close();
        doThrow(new IOException()).when(mockedResponse).close();

        Integer[] result = wrapper.submitHttpGetRequest(testUrl);

        assertThat(result.length, is(2));
        assertThat(result[0], is(1));
        assertThat(result[1], is(2));

        verify(mockedClient).execute(getCaptor.capture());
        verify(mockedResponse).close();
        verify(mockedStream).close();
    }

    /**
     * If the wrapped client returns a {@code null} entity {@code null} will
     * be returned.
     */
    @Test
    public void submitRequestNullEntity() throws IOException
    {
        String testUrl = "url";
        when(mockedClient.execute(any(HttpGet.class))).thenReturn(
                mockedResponse);
        when(mockedResponse.getEntity()).thenReturn(null);

        Integer[] result = wrapper.submitHttpGetRequest(testUrl);

        assertThat(result, nullValue());
    }

    /**
     * If the client throws an exception, {@code null} will be returned.
     * @throws IOException will not occur since the objects are mocked.
     */
    @Test
    public void submitRequestFail() throws IOException
    {
        when(mockedClient.execute(any(HttpGet.class)))
                .thenThrow(new IOException());

        assertThat(wrapper.submitHttpGetRequest("test"), nullValue());
    }

    /**
     * If the input stream throws an exception {@code null} will be returned.
     * All responses and streams will be closed.
     *
     * @throws IOException will not occur since the objects are mocked.
     */
    @Test
    public void submitRequestIoError() throws IOException
    {
        String testUrl = "url";

        when(mockedClient.execute(any(HttpGet.class))).thenReturn(
                mockedResponse);
        when(mockedResponse.getEntity()).thenReturn(mockedEntity);
        when(mockedEntity.getContent()).thenReturn(mockedStream);
        when(mockedStream.read()).thenThrow(new IOException());

        Integer[] result = wrapper.submitHttpGetRequest(testUrl);
        assertThat(result, nullValue());
        verify(mockedStream).close();
        verify(mockedResponse).close();
    }


    /**
     * Passing a {@code null} stream into the close method will not cause an
     * exception.
     */
    @Test
    public void nullStream()
    {
        wrapper.closeStream(null);
    }
}
