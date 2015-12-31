package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.search;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.sangcomz_n_study.bean.Member;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;

/**
 * Created by sangc on 2015-12-28.
 */
public class SeachController {
    Context context;
    SearchFriendFragment searchFriendFragment;

    public SeachController(Context context, SearchFriendFragment searchFriendFragment) {
        this.context = context;
        this.searchFriendFragment = searchFriendFragment;
    }


    public void SearchMember(String query, int page) {
        RequestParams params = new RequestParams();

        params.put("query", query);

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("page", page);
        HttpClient.get(UrlDefine.URL_SEARCH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("members");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Member>>() {
                    }.getType();
                    List<Member> members = (List<Member>) gson.fromJson(jsonOutput, listType);

                    searchFriendFragment.setMembers((ArrayList<Member>) members);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }
}