package db;

public class City {
	private int id, province_id;
	private String city_name;
	private String city_code;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCityName(String name) {
		city_name = name;
	}

	public String getCityName() {
		return city_name;
	}

	public void setCityCode(String code) {
		city_code = code;
	}

	public String getCityCode() {
		return city_code;
	}

	public void setProvinceId(int id) {
		province_id = id;
	}

	public int getProvinceId() {
		return province_id;
	}

}
