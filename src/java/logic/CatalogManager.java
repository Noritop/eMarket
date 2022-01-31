package logic;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import model.Product;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

// définir la durée de conservation de son scope: Application.
@ManagedBean
@ApplicationScoped
public class CatalogManager implements Serializable{
    // ProductList permettant de stocker le catalogue
    private ArrayList<Product> productList;
    // Variables du formulaire <h:form id="product-create">
    private int id;
    private String name;
    private double price;

    // Constructeur par defaut
    public CatalogManager(){
        this.productList = new ArrayList<>();
    }

    // Setter et getter productList
    public ArrayList<Product> getProductList(){
        return this.productList;
    }

    public void setProductList( ArrayList<Product> productList ){
        this.productList = productList;
    }

    // Setter et getter variables du formulaire Id, Name et Price
    public int getId() { return id; }

    public String getName() { return name; }

    public double getPrice() { return price; }
    
    public void setId( int id ) { this.id = id; }

    public void setName( String N ) { this.name = N; }

    public void setPrice( double P ) { this.price = P; }

    // Méthode appelée après la création du bean
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
    
    // Utilise les variables du formulaire pour créer un nouveau produit.
    // Puis, ajoute ce produit au catalogue si n'est pas dans le catalogue
    public String createProduct(){
        Product p;
        p = new Product(getId(), getName(), getPrice());

        boolean test = false;

        for ( Product element : productList ) {
            if ( p.equals(element) ) {
                test = true;
                break;
            }
        }

        if ( !test ) this.productList.add( p );

        return "tocatalog";
    }

}
