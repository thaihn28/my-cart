package hanu.a2_1901040197.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040197.R;
import hanu.a2_1901040197.SetTotalPrice;
import hanu.a2_1901040197.adapters.CartAdapter;
import hanu.a2_1901040197.db.ProductManager;
import hanu.a2_1901040197.models.Product;

public class CartFragment extends Fragment {
    private RecyclerView rcvCartView;
    private CartAdapter cartAdapter;
    List<Product> productList;
    public static TextView tvTotalPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rcvCartView = view.findViewById(R.id.rcvCartView);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        rcvCartView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProductManager productManager = ProductManager.getInstance(getActivity());
        productList = productManager.allProducts();
        cartAdapter = new CartAdapter(getActivity());

        long totalPrice = 0;
        for(Product p : productList){
            totalPrice += (p.getQuantity() * p.getUnitPrice());
        }
        SetTotalPrice.totalPrice(totalPrice);

        cartAdapter.setData(productList);
        rcvCartView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        return view;
    }

}