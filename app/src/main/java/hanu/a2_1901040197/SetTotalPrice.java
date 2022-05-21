package hanu.a2_1901040197;


import hanu.a2_1901040197.adapters.CartAdapter;
import hanu.a2_1901040197.fragment.CartFragment;

public class SetTotalPrice {
    public static void totalPrice(long totalPrice){
        CartFragment.tvTotalPrice.setText("₫ " + CartAdapter.FORMATTER.format(totalPrice));
    }
}
