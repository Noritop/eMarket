package model;

import java.io.Serializable;


public class Product implements Serializable{
  private int id;
  private String name;
  private double price;
  
 public Product(int id, String name, double price){
    this.id = id;
    this.name = name;
    this.price = price;
  }
 
 public int getId() { return id; }
 public String getName() { return name; }
 public double getPrice() { return price; }
 public void setId(int i) {this.id = i;}
 public void setName(String N) {this.name = N;}
 public void setPrice(double P) {this.price = P;}
 
}
