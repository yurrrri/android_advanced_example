package ddwu.com.mobile.multimedia.photomemo;

public class MemoDto {

    private long _id;
    private String photoPath;
    private String memo;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return new String(_id + " : " + photoPath);
    }
}
