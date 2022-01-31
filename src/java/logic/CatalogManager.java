package logic;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import model.Product;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;


@ManagedBean
@ApplicationScoped
public class CatalogManager implements Serializable{
    private ArrayList<Product> productList;
    private int id;
    private String name;
    private double price;

    public CatalogManager(){
        this.productList = new ArrayList<Product>();
    }

    public ArrayList<Product> getProductList(){ return this.productList; }
    public void setProductList(ArrayList<Product> l){ this.productList=l; }
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setId(int i) {this.id = i;}
    public void setName(String N) {this.name = N;}
    public void setPrice(double P) {this.price = P;}
    
    @PostConstruct
    public void myInitMethod(){
        Product p1;
        p1 = new Product(0, "Cannondale P-32", 2599);
        Product p2;
        p2 = new Product(1, "Giant Elbruz", 3650);
        Product p3;
        p3 = new Product(2, "Scott Erlin", 1499);

        this.productList.add(p1);
        this.productList.add(p2);
        this.productList.add(p3);
    }
    
    public String createProduct(){
        Product p;
        p = new Product(getId(), getName(), getPrice());
        this.productList.add(p);
        return "tocatalog";
    }

}
