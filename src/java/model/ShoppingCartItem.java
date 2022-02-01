package model;

import java.io.Serializable;

// Classe sérialisable représentant un produit du panier
public class ShoppingCartItem implements Serializable{
    // un identifiant de type Integer
    private int id;
    // une quantité de type Integer
    private int quantite;
    // un produit de type Product
    private Product produit;
  
    // Constructeur par défaut
    public ShoppingCartItem(int id, int quantite, Product produit){
       this.id = id;
       this.quantite = quantite;
       this.produit = produit;
     }

    // Getter et setter
    public int getId() { return this.id; }
    
    public int getQuantite() { return this.quantite; }
    
    public Product getProduit() { return this.produit; }
    
    public void setId(int i) {this.id = i;}
    
    public void setQuantite(int Q) {this.quantite = Q;}
    
    public void setProduit(Product P) {this.produit = P;}
}
