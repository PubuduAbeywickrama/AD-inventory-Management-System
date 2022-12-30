package tm;

public class itemsTM {
    private String itemCode;
    private String itemName;
    private Double dealerPrice;
    private Double sellingPrice;

    @Override
    public String toString() {
        return "itemsTM{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", dealerPrice=" + dealerPrice +
                ", sellingPrice=" + sellingPrice +
                ", quantity=" + quantity +
                '}';
    }

    private int quantity;

    public itemsTM(String itemCode, String itemName, Double dealerPrice, Double sellingPrice, int quantity) {
        this.setItemCode(itemCode);
        this.setItemName(itemName);
        this.setDealerPrice(dealerPrice);
        this.setSellingPrice(sellingPrice);
        this.setQuantity(quantity);
    }


    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getDealerPrice() {
        return dealerPrice;
    }

    public void setDealerPrice(Double dealerPrice) {
        this.dealerPrice = dealerPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
