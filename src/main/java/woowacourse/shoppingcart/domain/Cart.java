package woowacourse.shoppingcart.domain;

public class Cart {

    private Long id;
    private int price;
    private String imageUrl;
    private String name;
    private Long quantity;
    private boolean checked;

    public Cart() {
    }

    public Cart(final Long id, final Long quantity, final Product product, final boolean checked) {
        this(id, product.getPrice(), product.getImageUrl(), product.getName(), quantity, checked);
    }

    public Cart(final Long id, final int price, final String imageUrl, final String name, final Long quantity, final boolean checked) {
        this.id = id;
        this.price = price;
        this.imageUrl = imageUrl;
        this.name = name;
        this.quantity = quantity;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }
}
