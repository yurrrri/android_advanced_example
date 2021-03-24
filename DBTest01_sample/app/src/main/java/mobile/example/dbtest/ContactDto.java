package mobile.example.dbtest;

import java.io.Serializable;

public class ContactDto implements Serializable {

	private int id;
	private String name;
	private String phone;
	private String category;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return id + ". " + category + " - " + name + " (" + phone + ")";
	}
	
}
