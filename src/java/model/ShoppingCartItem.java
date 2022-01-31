package model;

import java.io.Serializable;


public class ShoppingCartItem implements Serializable{
    private int id;
    private int quantite;
    private Product produit;
  
    public ShoppingCartItem(int id, int quantite, Product produit){
       this.id = id;
       this.quantite = quantite;
       this.produit = produit;
     }

    public int getId() { return id; }
    public int getQuantite() { return quantite; }
    public Product getProduit() { return produit; }
    public void setId(int i) {this.id = i;}
    public void setQuantite(int Q) {this.quantite = Q;}
    public void setProduit(Product P) {this.produit = P;}
 
}
