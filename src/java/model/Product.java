package model;

import java.io.Serializable;

// Classe sérialisable représentant un produit
public class Product implements Serializable{
  // identifiant du produit
  private int id;
  // nom du produit
  private String name;
  // prix du produit
  private double price;
 
    // constructeur par défaut 
    public Product(int id, String name, double price){
       this.id = id;
       this.name = name;
       this.price = price;
     }

    // créer des accesseurs pour chacun des attributs
    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public double getPrice() { return this.price; }

    public void setId(int i) {this.id = i;}

    public void setName(String N) {this.name = N;}

    public void setPrice(double P) {this.price = P;}

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if (!( obj instanceof Product ) )return false;

        Product other = (Product) obj;

        // if ( id == other.getId() ) return true;

        if ( name.equals( other.getName() ) ) return true;

        return false;
    }
}
