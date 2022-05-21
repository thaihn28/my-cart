package hanu.a2_1901040197.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040197.R;
import hanu.a2_1901040197.adapters.ProductAdapter;
import hanu.a2_1901040197.models.Product;

public class ProductFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private EditText edtSearch;
    private ImageButton btnSearch;
    private static final String JSON_URL = "https://mpr-cart-api.herokuapp.com/products";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = view.findViewById(R.id.rcvProductView);
        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        RestLoader restLoader = new RestLoader();
        restLoader.execute(JSON_URL);

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity());
        productAdapter.setData(products);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(productAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                searchByName(editable.toString().trim());
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {

    }
    public List<Product> searchByName(String name){
        ArrayList<Product> searchedProduct = new ArrayList<>();
        for (Product p : products){
            if(p.getName().toLowerCase().contains(name.toLowerCase())){
                searchedProduct.add(p);
            }
        }
        productAdapter.setData(searchedProduct);
        productAdapter.notifyDataSetChanged();
        return searchedProduct;
    }

    public class RestLoader extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == null){
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String thumbnail = jsonObject.getString("thumbnail");
                    String name = jsonObject.getString("name");
                    int unitPrice = jsonObject.getInt("unitPrice");
                    products.add(new Product(id, thumbnail, name, unitPrice));
                }
                productAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
