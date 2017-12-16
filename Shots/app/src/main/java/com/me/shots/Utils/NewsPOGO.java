package com.me.shots.Utils;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Half_BlooD PrincE on 10/9/2017.
 */

public class NewsPOGO {


    public static ArrayList<NewsPOGO> newsArray =new ArrayList<>();

    public String body;
    public String category;
    public String content_type;
    public int id;
    public String image;
    public int likes;
    public String link;
    public String timestamp;
    public String title;
    public String types;
    public int user_id;
    public Bitmap news_image=null;
    public Bitmap blurred_news_image=null;

    public NewsPOGO()
    {

    }
}
