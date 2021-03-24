package mobile.example.network.test.myxmlparser;

import java.io.Serializable;

//intent 전달시 Serializable 구현 필요
public class DailyBoxOffice implements Serializable {
    private long _id;
    private String rank;
    private String movieNm;
    private String openDt;
    private String movieCD;

//    default constructor
    public DailyBoxOffice() {}

    public DailyBoxOffice(String rank, String movieNm, String openDt, String movieCD) {
        this.rank = rank;
        this.movieNm = movieNm;
        this.openDt = openDt;
        this.movieCD = movieCD;
    }

    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id;  }
    public String getMovieNm() {
        return movieNm;
    }
    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }
    public String getOpenDt() {
        return openDt;
    }
    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getMovieCD() { return movieCD; }
    public void setMovieCD(String movieCD) { this.movieCD = movieCD; }
    
    @Override
    public String toString() {
        return rank + ". '" + movieNm + "\'" +
                " (" + openDt + " 개봉)";
    }
}

