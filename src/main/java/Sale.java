/**
 * Created by YaHeng on 2017-05-30.
 */
public class Sale {
    private Product product;
    private int quantity;
    private Customer customer;
    public Sale(Product product, int quantity, Customer customer)
    {
        this.product=product;
        this.quantity=quantity;
        this.customer=customer;
    }

    public double getRevenue()
    {
        double revenue = product.getCost()*quantity;
        return revenue;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
