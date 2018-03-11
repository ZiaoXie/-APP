package com.example.timemanagement.ShoppingMall;

public class Skin {
	
		private String name;
		public String price;
		private int imageid;
		public Skin(String name,String price,int imageid){
			this.name=name;
			this.price=price;
			this.imageid=imageid;
		}
		
		public String getname(){
			return name;
		}
		public String getprice(){
			return  price;
		}
		public int getimageid(){
			return imageid;
		}
}
