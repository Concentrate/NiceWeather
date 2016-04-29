package db;

public class Country {
	private int id, city_id;
	private String country_name;
	private String country_code;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCityId(int id) {
		this.city_id = id;
	}

	public int getCityId() {
		return city_id;
	}

	public void setCountryName(String name) {
		country_name = name;
	}

	public String getCountryName() {
		return country_name;
	}

	public void setCountryCode(String code) {
		country_code = code;
	}

	public String getCountryCode() {
		return country_code;
	}

}
