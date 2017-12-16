package com.me.shots;

import java.io.Serializable;

/**
 * Created by J Girish on 16-12-2017.
 */

public class domain_courses implements Serializable{
    String domain;
    int id;
    int in_progress_count;
    String url;
    String title;
    public domain_courses()
    {

    }
    public domain_courses(String domain,int id,int in_progress_count,String url,String title)
    {
        this.id=id;
        this.in_progress_count=in_progress_count;
        this.domain=domain;
        this.url=url;
        this.title=title;
    }

    public  void setDomain(String domain)
    {
        this.domain=domain;
    }
    public  void setId(int id)
    {
        this.id=id;
    }
    public  void setIn_progress_count(int in_progress_count)
    {
        this.in_progress_count=in_progress_count;
    }
    public  void setUrl(String url)
    {
        this.url=url;
    }
    public  void setTitle(String title)
    {
        this.title=title;
    }
    public int getId()
    {
        return id;
    }
    public int getIn_progress_count()
    {
        return in_progress_count;
    }
    public String getDomain()
    {
        return domain;
    }
    public  String getUrl() {
        return url;
    }
    public String  getTitle()
    {
        return title;
    }
}
