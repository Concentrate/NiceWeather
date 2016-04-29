package db;

public class Province {
	private int id;
	private String province_name;
	private String province_code;

	public void setId(int num) {
		this.id = num;
	}

	public int getId() {
		return id;
	}

	public void setProvinceName(String name) {
		province_name = name;
	}

	public String getProvinceName() {
		return province_name;
	}

	public void setProvinceCode(String code) {
		province_code = code;
	}

	public String getProvinceCode() {
		return province_code;
	}
}
