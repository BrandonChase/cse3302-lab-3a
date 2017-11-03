package cities;

public class zipCodeClass {
	public zipCodeClass(int zipCode, String typeZip, String cityName, String countyName, int estPop) {
		this.zipCode = zipCode;
		this.typeZip = typeZip;
		this.cityName = cityName;
		this.countyName = countyName;
		this.estPop = estPop;
	}
	
	private int zipCode;
	private String typeZip;
	private String cityName;
	private String countyName;
	private int estPop;
	
	public int getZipCode() { return zipCode; }
	public String getTypeZip() { return typeZip; }
	public String getCityName() { return cityName; }
	public String getCountyName() { return countyName; }
	public int getEstPop() { return estPop; }
}
