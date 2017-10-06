package model;

/**
 * Created by msp on 17/8/16.
 */
public class BasicCMS {

    private String pageid;
    private String page_title;
    private String contenturl;

    @Override
    public String toString() {
        return "BasicCMS{" +
                "pageid='" + pageid + '\'' +
                ", page_title='" + page_title + '\'' +
                ", contenturl='" + contenturl + '\'' +
                '}';
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
