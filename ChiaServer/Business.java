public abstract class Business {

	private int id;
	private String name;
	private String phoneNumber;
	private String stNumber;
	private String stName;
	private String postalcode;
    
	
	/*
	 * getters and setters for all instance variables
	 */
	
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNum) {
		this.phoneNumber = phoneNum;
	}

	public String getStNumber() {
		return stNumber;
	}

	public void setStNumber(String sNum) {
		this.stNumber = sNum;
	}

	public String getStName() {
		return stName;
	}

	public void setStName(String stN) {
		this.stName = stN;
	}


	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String pCode) {
		this.postalcode = pCode;
	}
	
	/*
	 * prints out all information
	 * 
	 */
	public String toString() {
		return "Store Name= " +  name + " Phne Number=" + phoneNumber + " St Number=" + stNumber + " St Name=" + stName + " Postalcode=" +
				postalcode;
	}
}


