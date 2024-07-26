package cafe;
/*
 MENU                                               VARCHAR2(21)
 COF                                                NUMBER
 NUM                                                NUMBER
 PRICE                                              NUMBER
 TEMP                                               VARCHAR2(15)
 DENSITY                                            VARCHAR2(15)
 TAKE                                               VARCHAR2(15)
 ICE                                                VARCHAR2(15)
 */
public class MenuDTO {
		private String menu;
	 	private int cof;
	    private int num;
	    private int price;
	    private String temp;
	    private String density;
	    private String take;
	    private String ice;
	    
		public String getMenu() {
			return menu;
		}

		public void setMenu(String menu) {
			this.menu = menu;
		}

	    public int getCof() {
	        return cof;
	    }

	    public int getNum() {
	        return num;
	    }

	    public int getPrice() {
	        return price;
	    }

	    public String getTemp() {
	        return temp;
	    }

	    public String getDensity() {
	        return density;
	    }

	    public String getTake() {
	        return take;
	    }

	    public void setCof(int cof) {
	        this.cof = cof;
	    }

	    public void setNum(int num) {
	        this.num = num;
	    }

	    public void setPrice(int price) {
	        this.price = price;
	    }

	    public void setTemp(String temp) {
	        this.temp = temp;
	    }

	    public void setDensity(String density) {
	        this.density = density;
	    }

	    public void setTake(String take) {
	        this.take = take;
	    }

		public String getIce() {
			return ice;
		}

		public void setIce(String ice) {
			this.ice = ice;
		}


}
