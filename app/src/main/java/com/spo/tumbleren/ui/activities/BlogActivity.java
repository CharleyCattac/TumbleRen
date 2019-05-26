package com.spo.tumbleren.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.spo.tumbleren.R;
import com.spo.tumbleren.ui.adapters.PostAdapter;
import com.spo.tumbleren.network.client.RenumblrClient;
import com.tumblr.jumblr.types.AudioPost;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.LinkPost;
import com.tumblr.jumblr.types.Post;
import com.tumblr.jumblr.types.VideoPost;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import static com.spo.tumbleren.utils.connection.ConnectionStateChecker.getConnectionState;
import static com.spo.tumbleren.utils.shared.Shared.OnPrefChanged;
import static com.spo.tumbleren.utils.shared.Shared.getDefaults;
import static com.spo.tumbleren.utils.shared.Shared.setDefaults;

public class BlogActivity extends AppCompatActivity {

    private Stack<String> blogStack;
    private boolean invalidNameFlag = false;
    private final String DEFAULT_NAME = "charleycattac.tumblr.com";

    private String currentBlogAvatarURL;
    private Blog currentBlog;
    private List<Post> currentPosts;

    private Toolbar toolbar;
    private ImageView blogAvatar;
    private TextView blogName;
    private TextView blogTitle;
    private TextView blogDescribtion;
    private TextView likesAmount;
    private TextView postsAmount;
    private TextView followersAmount;

    private RecyclerView postsView;
    private PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        blogStack = new Stack<>();

        initActivityViews();
        initRecyclerView();
        defineNameInPreferences(DEFAULT_NAME);

        updateActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OnPrefChanged){
            fillBlog();
        }
        else {
            OnPrefChanged = false;
            updateActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postAdapter.clearItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.update_item, menu);
        getMenuInflater().inflate(R.menu.settings_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_update:{
                updateActivity();
                break;
            }
            case R.id.action_settings:{
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (blogStack.empty())
            super.onBackPressed();
        else{
            defineNameInPreferences(blogStack.pop());
            updateActivity();
        }
    }

    private void initActivityViews() {
        toolbar = findViewById(R.id.blog_toolbar);
        setSupportActionBar(toolbar);
        blogAvatar = findViewById(R.id.blog_avatar);
        blogName = findViewById(R.id.blog_name);
        blogTitle = findViewById(R.id.blog_title);
        blogDescribtion = findViewById(R.id.blog_describtion);
        likesAmount = findViewById(R.id.blog_likes_amount);
        postsAmount = findViewById(R.id.blog_posts_amount);
        followersAmount = findViewById(R.id.blog_followers_amount);

        postsView = findViewById(R.id.posts_view);
    }

    private void initRecyclerView() {
        ViewCompat.setNestedScrollingEnabled(postsView, false);
        postsView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        postsView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String text) {
                try {
                    if(text.contains("/post/")){
                        Intent jump = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                        startActivity(jump);
                    }
                    else {
                        if(text.length() > 0) {
                            blogStack.push(currentBlog.getName());
                            defineNameInPreferences(text);
                            updateActivity();
                        }
                        else
                            Toast.makeText(BlogActivity.this, "Cannot open blog!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(BlogActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onItemClick(LinkPost linkPost) {
                try {
                    String url = linkPost.getLinkUrl();
                    if(url != null) {
                        Intent jump = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(jump);
                    }
                    else
                        Toast.makeText(BlogActivity.this, "Cannot open link!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(BlogActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onItemClick(VideoPost videoPost) {
                Toast.makeText(BlogActivity.this, "Click on the link below to play the video!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemClick(AudioPost audioPost) { }
        });
        postsView.setAdapter(postAdapter);
    }

    private void updateActivity(){
        if (getConnectionState(this)){
            ImportBlogInAsyncTask updateBlog = new ImportBlogInAsyncTask();
            updateBlog.execute();
        }
        else
            Toast.makeText(BlogActivity.this, "Check internet connection!", Toast.LENGTH_SHORT).show();
    }

    private void defineNameInPreferences(String name){
        setDefaults("name", name, BlogActivity.this);
        setDefaults("type", "", BlogActivity.this);
        setDefaults("tag", "", BlogActivity.this);
        setDefaults("limit", "", BlogActivity.this);
    }

    private Map<String, String> getParamsFromSharedPreferences() {
        Map<String, String> params = new HashMap<>();

        params.put("name", getDefaults("name", BlogActivity.this));
        params.put("type", getDefaults("type", BlogActivity.this));
        params.put("tag", getDefaults("tag", BlogActivity.this));
        params.put("limit", getDefaults("limit", BlogActivity.this));
        params.put("filter","text");

        return params;
    }

    private void fillBlog(){
        try {
            if (invalidNameFlag) {
                invalidNameFlag = false;
                Toast.makeText(BlogActivity.this, "Blog not found! Default name is used.", Toast.LENGTH_LONG).show();
            }
            BlogActivity.this.setTitle(currentBlog.getName());
            blogName.setText(currentBlog.getName());
            blogTitle.setText(currentBlog.getTitle());
            blogDescribtion.setText(currentBlog.getDescription());

            likesAmount.setText(String.format(Locale.getDefault(),"%d", currentBlog.getLikeCount()));
            if (likesAmount.getText().toString().equals("0"))
                likesAmount.setVisibility(View.GONE);
            postsAmount.setText(String.format(Locale.getDefault(),"%d", currentBlog.getPostCount()));
            if (postsAmount.getText().toString().equals("0"))
                postsAmount.setVisibility(View.GONE);
            followersAmount.setText(String.format(Locale.getDefault(),"%d", currentBlog.getFollowersCount()));
            if (followersAmount.getText().toString().equals("0"))
                followersAmount.setVisibility(View.GONE);
            Picasso.get().load(currentBlogAvatarURL).resize(256, 256).centerCrop().into(blogAvatar);

            fillPosts();
        } catch (Exception ex) {
            Toast.makeText(BlogActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillPosts(){
        postAdapter.setItems(currentPosts);
        if (currentPosts.size() == 0)
            Toast.makeText(BlogActivity.this, "No suitable posts!", Toast.LENGTH_LONG).show();
    }

    private class ImportBlogInAsyncTask extends AsyncTask<String, Void, Boolean> {
        private final ProgressDialog dialog = new ProgressDialog(BlogActivity.this);
        private RenumblrClient client;
        Map<String, String> params;
        String name;

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();

            if (currentBlog == null)
                currentBlog = new Blog();

            client = RenumblrClient.getInstance();
            params = getParamsFromSharedPreferences();
            name = params.get("name");

            params.remove("name");

            if(params.get("type") != null)
                if(params.get("type").isEmpty())
                    params.remove("type");
            if(params.get("tag") != null)
                if((params.get("tag")).isEmpty())
                    params.remove("tag");
            if(params.get("limit") != null)
                if(params.get("limit").isEmpty())
                    params.remove("limit");
            if (params.get("filter") != null)
                if (params.get("filter").isEmpty())
                    params.remove("filter");
        }

        protected Boolean doInBackground(final String... args) {
            // Make the requests
            try{
                currentBlog = client.blogInfo(name);
                currentBlogAvatarURL = client.blogAvatar(name);
                currentPosts = client.blogPosts(name, params);
            } catch (Exception ex) {
                invalidNameFlag = true;
                currentBlog = client.blogInfo(DEFAULT_NAME);
                currentBlogAvatarURL = client.blogAvatar(DEFAULT_NAME);
                currentPosts = client.blogPosts(DEFAULT_NAME, params);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
                fillBlog();
            }
        }
    }

}