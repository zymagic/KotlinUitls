package com.zy.kotlinutils.core.http;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zy on 17-6-12.
 */

public class Http {
    private static final String CONTENT_TYPE_PLAIN_TEXT = "";
    private static final String CONTENT_TYPE_IMAGE = "";
    private static final String CONTENT_TYPE_FILE = "";
    public enum HttpMethod {
        GET("GET"), POST("POST");
        String name;
        HttpMethod(String name) {
            this.name = name;
        }
    }

    private HttpMethod method;
    private String url;
    private HttpParams params;

    public static Http create(HttpMethod method) {
        return new Http(method);
    }

    private Http(HttpMethod method) {
        this.method = method;
    }

    public Http url(String url) {
        this.url = url;
        return this;
    }

    public Http param(HttpParams params) {
        this.params = params;
        return this;
    }

    public StringResult getStringResult() {
        return new StringResult(getResult(new StringParser()));
    }

    public ImageResult getImageResult() {
        return new ImageResult(getResult(new BitmapParser()));
    }

    public <T> Result<T> getResult(ResultParser<T> parser) {
        Result<T> result = new Result<T>();
        switch (method) {
            case GET:
                get(parser, result);
            case POST:
                break;
        }
        return result;
    }

    public Status download(File file) {
        return null;
    }

    private <T> void get(ResultParser<T> parser, Result<T> out) {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(makeUrl(this.url, this.params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.name);
            conn.setDoInput(true);
            conn.connect();
            out.statusCode = conn.getResponseCode();
            out.msg = conn.getResponseMessage();
            if (out.statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("failed to connect to " + url + ", " + out.statusCode + ", " + out.msg);
            }
            is = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            os = bos;
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            out.object = parser.parse(bos.toByteArray());
        } catch (MalformedURLException e) {
            out.msg = "malformed url";
        } catch (IOException e) {
            out.msg = e.getMessage();
        } finally {
//            IOUtils.close(is);
//            IOUtils.close(os);
        }
    }

    private <T> void get(StreamParser<T> parser, Result<T> out) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(makeUrl(this.url, this.params));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.name);
            conn.setDoInput(true);
            conn.connect();
            out.statusCode = conn.getResponseCode();
            out.msg = conn.getResponseMessage();
            if (out.statusCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("failed to connect to " + url + ", " + out.statusCode + ", " + out.msg);
            }
            is = conn.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            os = bos;
            byte[] buffer = new byte[4096];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                parser.update(buffer, len);
            }
            os.flush();
            out.object = parser.parse();
        } catch (MalformedURLException e) {
            out.msg = "malformed url";
        } catch (IOException e) {
            out.msg = e.getMessage();
        } finally {
//            IOUtils.close(is);
//            IOUtils.close(os);
        }
    }

    private <T> void post(ResultParser<T> parser, Result<T> out) {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.name);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            os = conn.getOutputStream();


        } catch (MalformedURLException e) {
            out.msg = "malformed url";
        } catch (IOException e) {
            out.msg = e.getMessage();
        } finally {
//            IOUtils.close(is);
//            IOUtils.close(os);
        }
    }

    private String makeUrl(String url, HttpParams params) {
        if (params == null || params.params.isEmpty()) {
            return url;
        }
        Map<String, Object> map = params.getMap(CONTENT_TYPE_PLAIN_TEXT);
        if (map.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf('?') > 0) {
            sb.append('?');
        }
        boolean first = url.indexOf('&') < 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append('&');
            }
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    public static class HttpParams {
        Map<String, Map<String, Object>> params;
        public HttpParams() {
            params = new HashMap<>();
        }

        public HttpParams add(String key, String value) {
            getMap(CONTENT_TYPE_PLAIN_TEXT).put(key, value);
            return this;
        }

        public HttpParams addImage(String key, Bitmap image) {
            getMap(CONTENT_TYPE_IMAGE).put(key, image);
            return this;
        }

        public HttpParams addFile(String key, String file) {
            getMap(CONTENT_TYPE_FILE).put(key, file);
            return this;
        }

        private Map<String, Object> getMap(String contentType) {
            Map<String, Object> map = params.get(contentType);
            if (map == null) {
                map = new HashMap<String, Object>();
                params.put(contentType, map);
            }
            return map;
        }
    }

    public static class Status {
        int statusCode;
        String msg;
    }

    public static class Result<T> extends Status {
        T object;
    }

    public interface ResultParser<T> {
        T parse(byte[] data);
    }

    public interface StreamParser<T> {
        void update(byte[] data, int len);
        T parse();
    }

    public static class StringResult extends Result<String> {
        StringResult(Result<? extends String> obj) {
            this.statusCode = obj.statusCode;
            this.object = obj.object;
        }
    }

    public static class ImageResult extends Result<Bitmap> {
        ImageResult(Result<? extends Bitmap> obj) {
            this.statusCode = obj.statusCode;
            this.object = obj.object;
        }
    }

    private static class StringParser implements ResultParser<String> {

        @Override
        public String parse(byte[] data) {
            return null;
        }
    }

    private static class BitmapParser implements ResultParser<Bitmap> {

        @Override
        public Bitmap parse(byte[] data) {
            return null;
        }
    }
}
