package com.softsquared.android.corona.src.main.community.interfaces;

import com.softsquared.android.corona.src.main.community.model.Comment;
import com.softsquared.android.corona.src.main.community.model.Post;

import java.util.ArrayList;

public interface PostDetailView {

    void getPostDetail(Post post, ArrayList<Comment> arrayList);

    void postCommentWrite();

    void validateFailure(String message);

    void postLikeSuccess();

}
