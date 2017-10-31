package model;

import java.io.Serializable;

/**
 * Created by msp on 17/8/16.
 */
public class BasicCMS implements Serializable{

    private String pageid;
    private String page_title;
    private String contenturl;

    

    public BasicCMS() {
		super();
		this.pageid = "";
		this.page_title = "";
		this.contenturl = "";
	}

	public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getContenturl() {
        return contenturl;
    }

    public void setContenturl(String contenturl) {
        this.contenturl = contenturl;
    }
}
