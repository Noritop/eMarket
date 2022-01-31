package logic;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import model.ShoppingCartItem;
import model.Product;


@ManagedBean
@ApplicationScoped
public class ShoppingCartManager implements Serializable{
    private ArrayList<ShoppingCartItem> shoppingList;
    private Product prodToAdd;

    public ShoppingCartManager(){
        this.shoppingList = new ArrayList<>();
    }

    public ArrayList<ShoppingCartItem> getShoppingList(){ return shoppingList; }
    public void setShoppingList(ArrayList<ShoppingCartItem> l){ this.shoppingList=l; }
    public Product getProdToAdd(){ return prodToAdd; }
    public void setProdToAdd(Product p){ this.prodToAdd=p; }
    
    @PostConstruct
    public void initCart(){
        ShoppingCartItem s1;
        ShoppingCartItem s2;
        
        Product p1;
        p1 = new Product(0, "Cannondale P-32", 2599);
        Product p2;
        p2 = new Product(1, "Giant Elbruz", 3650);
        
        s1 = new ShoppingCartItem(0, 1, p1);
        s2 = new ShoppingCartItem(0, 2, p2);

        this.shoppingList.add(s1);
        this.shoppingList.add(s2);
    }
    
    public String addToCart(){
        ShoppingCartItem s;
        s = new ShoppingCartItem(0, 1, getProdToAdd());
        this.shoppingList.add(s);
        return "toshoppingcart";
    }

}
