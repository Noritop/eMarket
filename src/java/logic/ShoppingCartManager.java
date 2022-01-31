package logic;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.ShoppingCartItem;
import model.Product;

// définir la durée de conservation de son scope: Session.
@ManagedBean
@SessionScoped
public class ShoppingCartManager implements Serializable{
    // attribut permettant de stocker le panier
    private ArrayList<ShoppingCartItem> shoppingList;
    private Product prodToAdd;
    private ShoppingCartItem itemToRmv;

    // Constructeur par defaut
    public ShoppingCartManager(){
        this.shoppingList = new ArrayList<>();
    }

    // Setter et getter 
    public ArrayList<ShoppingCartItem> getShoppingList(){ return shoppingList; }
    public void setShoppingList(ArrayList<ShoppingCartItem> l){ this.shoppingList=l; }
    public Product getProdToAdd(){ return prodToAdd; }
    public void setProdToAdd(Product p){ this.prodToAdd=p; }
    public ShoppingCartItem getItemToRmv(){ return itemToRmv; }
    public void setItemToRmv(ShoppingCartItem p){ this.itemToRmv=p; }

    @PostConstruct
    public void initCart(){
        ShoppingCartItem s1;
        ShoppingCartItem s2;
        
        Product p1;
        p1 = new Product(0, "Cannondale P-32", 2599);
        Product p2;
        p2 = new Product(1, "Giant Elbruz", 3650);
        
        s1 = new ShoppingCartItem(0, 1, p1);
        s2 = new ShoppingCartItem(1, 2, p2);

        this.shoppingList.add(s1);
        this.shoppingList.add(s2);
    }
    
    // Ajout du produit dans le panier
    public String addToCart(){
        ShoppingCartItem s;
        s = new ShoppingCartItem( shoppingList.size() , 1, getProdToAdd());

        boolean test = false;

        for ( ShoppingCartItem element : shoppingList ) {
            if ( prodToAdd.equals( element.getProduit() ) ) {
                test = true;
                element.setQuantite( element.getQuantite() + 1 );
                break;
            }
        }

        if ( !test ) this.shoppingList.add(s);

        return "toshoppingcart";
    }

    // suppression de produits se trouvant déjà dans le panier
    public String rmvfromCart(){

        for ( ShoppingCartItem element : shoppingList ) {
            if ( itemToRmv.getProduit().equals( element.getProduit() ) ) {
                shoppingList.remove(element);
                break;
            }
        }

        return "toshoppingcart";
    }
}
