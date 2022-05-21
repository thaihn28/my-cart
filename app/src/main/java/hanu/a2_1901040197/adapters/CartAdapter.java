package hanu.a2_1901040197.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import hanu.a2_1901040197.R;
import hanu.a2_1901040197.SetTotalPrice;
import hanu.a2_1901040197.db.ProductManager;
import hanu.a2_1901040197.models.Product;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<Product> productList;
    ProductManager productManager;
    public static final DecimalFormat FORMATTER = new DecimalFormat("###,###,###");

    public CartAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Product> products){
        this.productList = products;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        if(product == null){
            return;
        }
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartThumbnail, btnAddQuantity, btnRemoveQuantity;
        TextView tvCartName, tvCartPrice, tvCartQuant, tvCartSumPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCartThumbnail = itemView.findViewById(R.id.imgCartThumbnail);
            btnAddQuantity = itemView.findViewById(R.id.btnAddQuantity);
            btnRemoveQuantity = itemView.findViewById(R.id.btnRemoveQuantity);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuant = itemView.findViewById(R.id.tvCartQuantity);
            tvCartSumPrice = itemView.findViewById(R.id.tvCartSumPrice);
        }
        public void bind(Product product, int position){
            productManager = ProductManager.getInstance(context);
            // Product exist -> set quantity ++ & set sum price again

            // set thumbnail
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(product.getThumbnail());
            // set name
            tvCartName.setText(product.getName());
            // set unit price
            Long unitPrice = product.getUnitPrice();
            tvCartPrice.setText(FORMATTER.format(unitPrice) + "₫ ");
            // set quantity
            tvCartQuant.setText("" + product.getQuantity());
            // set Sum Price
            long sumPrice = product.getQuantity() * product.getUnitPrice();
            tvCartSumPrice.setText(FORMATTER.format(sumPrice) + "₫ " );

            // btn Add & Remove
            btnAddQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    product.setQuantity(product.getQuantity() + Long.valueOf(1));
                    productManager.update(product.getQuantity(), product.getId());
                    // set Total price
                    Long totalPrice = Long.valueOf(0);
                    for (Product p : productList){
                        totalPrice += (p.getUnitPrice() * p.getQuantity());
                    }
                    SetTotalPrice.totalPrice(totalPrice);
                    notifyDataSetChanged();
                }
            });
            btnRemoveQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(product.getQuantity() != 1){

                        product.setQuantity(product.getQuantity() - Long.valueOf(1));
                        productManager.update(product.getQuantity(), product.getId());
                        Long totalPrice = Long.valueOf(0);
                        for (Product p : productList){
                            totalPrice += (p.getUnitPrice() * p.getQuantity());
                        }
                        SetTotalPrice.totalPrice(totalPrice);
                        notifyDataSetChanged();
                    }
                    else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Delete Confirmation!").setMessage("Do you want delete '" + product.getName() + "'").setIcon(android.R.drawable.ic_delete);

                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                productManager.delete(product.getId());
                                productList.remove(product);
                                long totalPrice = 0;
                                for (Product p : productList){
                                    totalPrice += (p.getQuantity() * p.getUnitPrice());
                                }
                                SetTotalPrice.totalPrice(totalPrice);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted product with ID " + product.getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    notifyDataSetChanged();
                }
            });
        }
        public class ImageLoader extends AsyncTask<String, Void, Bitmap>{
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imgCartThumbnail.setImageBitmap(bitmap);
            }
    }
    }
}
