package ddwucom.mobile.myonepick;

import java.io.Serializable;

/*
하나의 주소 정보를 저장하기 위한 DTO
Intent 에 저장 가능하게 하기 위하여
Serializable 인터페이스를 구현함
*/

public class PlaceDto implements Serializable {

	private long id;
	private String category;
	private String name;
	private String review;
	private float rate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return id + ". " + category + " - " + name + ", 평점 " + rate + "점 : " + review;
	}
	
}
