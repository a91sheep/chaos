package com.store59.printapi.model;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.stream.IntStream;


/**
 * Created by heqingpan on 16/2/26.
 */
public class StreamBody implements ContentBody {
    String      name;
    byte[]      data;
    ContentType contentType;

    public StreamBody(String name, byte[] data) {
        this.name = name;
        this.data = data;
        this.contentType = ContentType.DEFAULT_BINARY;
    }

    @Override
    public String getFilename() {
        return this.name;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.data);
    }

    @Override
    public String getMimeType() {
        return contentType.getMimeType();
    }

    @Override
    public String getMediaType() {
        String mimeType = this.contentType.getMimeType();
        int i = mimeType.indexOf(47);
        return i != -1?mimeType.substring(0, i):mimeType;
    }

    @Override
    public String getSubType() {
        String mimeType = this.contentType.getMimeType();
        int i = mimeType.indexOf(47);
        return i != -1?mimeType.substring(i + 1):null;
    }

    @Override
    public String getCharset() {
        Charset charset = this.contentType.getCharset();
        return charset != null?charset.name():null;
    }

    @Override
    public String getTransferEncoding() {
        return "binary";
    }

    @Override
    public long getContentLength() {
        return this.data.length;
    }
}
