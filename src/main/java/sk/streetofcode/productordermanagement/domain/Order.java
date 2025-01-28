package sk.streetofcode.productordermanagement.domain;

import lombok.Setter;

import java.util.List;

public final class Order {
    private final long id;
    @Setter
    private List<ShoppingList> shoppingLists;
    private final Boolean paymentStatus;

    public Order(long id, List<ShoppingList> shoppingLists, Boolean paymentStatus) {
        this.id = id;
        this.shoppingLists = shoppingLists;
        this.paymentStatus = paymentStatus;
    }


    public long getId() {
        return this.id;
    }

    public List<ShoppingList> getShoppingLists() {
        return this.shoppingLists;
    }

    public Boolean getPaymentStatus() {
        return this.paymentStatus;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Order)) return false;
        final Order other = (Order) o;
        if (this.getId() != other.getId()) return false;
        final Object this$shoppingLists = this.getShoppingLists();
        final Object other$shoppingLists = other.getShoppingLists();
        if (this$shoppingLists == null ? other$shoppingLists != null : !this$shoppingLists.equals(other$shoppingLists))
            return false;
        final Object this$paymentStatus = this.getPaymentStatus();
        final Object other$paymentStatus = other.getPaymentStatus();
        if (this$paymentStatus == null ? other$paymentStatus != null : !this$paymentStatus.equals(other$paymentStatus))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $id = this.getId();
        result = result * PRIME + (int) ($id >>> 32 ^ $id);
        final Object $shoppingLists = this.getShoppingLists();
        result = result * PRIME + ($shoppingLists == null ? 43 : $shoppingLists.hashCode());
        final Object $paymentStatus = this.getPaymentStatus();
        result = result * PRIME + ($paymentStatus == null ? 43 : $paymentStatus.hashCode());
        return result;
    }

    public String toString() {
        return "Order(id=" + this.getId() + ", shoppingLists=" + this.getShoppingLists() + ", paymentStatus=" + this.getPaymentStatus() + ")";
    }
}

