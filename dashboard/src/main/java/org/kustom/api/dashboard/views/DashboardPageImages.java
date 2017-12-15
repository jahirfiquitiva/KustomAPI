package org.kustom.api.dashboard.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kustom.api.dashboard.ImagePreviewActivity;
import org.kustom.api.dashboard.model.ImageItem;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DashboardPageImages extends DashboardPage<ImageItem> {
    private final OkHttpClient client = new OkHttpClient();

    public DashboardPageImages(@NonNull Context context) {
        super(context);
    }

    public void setUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                setText(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<ImageItem> list = new ArrayList<>();
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String body = responseBody != null ? responseBody.string() : "";
                    if (TextUtils.isEmpty(body))
                        throw new IOException("Empty data");
                    try {
                        JSONObject data = new JSONObject(body);
                        JSONArray walls = data.getJSONArray("wallpapers");
                        for (int i = 0; i < walls.length(); i++) {
                            list.add(new ImageItem(walls.getJSONObject(i)));
                        }
                    } catch (JSONException e) {
                        throw new IOException("Invalid JSON " + e.getMessage());
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
                setEntries(list);
            }
        });
    }

    @Override
    public boolean onClick(ImageItem item) {
        Intent intent = new Intent(getContext(), ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.EXTRA_IMAGE_DATA, item.getImageData().getJsonData());
        getContext().startActivity(intent);
        return false;
    }
}
