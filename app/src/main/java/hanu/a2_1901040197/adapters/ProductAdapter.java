package hanu.a2_1901040197.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import java.util.List;

import hanu.a2_1901040197.R;
import hanu.a2_1901040197.db.ProductManager;
import hanu.a2_1901040197.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    ProductManager productManager;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> products) {
        this.productList = products;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        holder.bind(product, position);
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvName, tvPrice;
        private ImageButton btnAddCart;
        private List<Product> dataListProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);

        }
        public void bind(Product product, int position) {
            tvName.setText(product.getName());
            tvPrice.setText("₫ " + CartAdapter.FORMATTER.format(product.getUnitPrice()));

            // set product image
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(product.getThumbnail());

            btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productManager = ProductManager.getInstance(context);
                    Boolean isExist = false;

                    for (Product dbProduct : productManager.allProducts()) {
                        if (product.getId() == dbProduct.getId()) {
                            dbProduct.setQuantity(dbProduct.getQuantity() + 1);
                            productManager.update(dbProduct.getQuantity(), dbProduct.getId());
                            isExist = true;
                            Toast.makeText(context, "Product has already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!isExist) {
                        Long id = product.getId();
                        String thumbnail = product.getThumbnail();
                        String name = product.getName();
                        Long unitPrice = product.getUnitPrice();
                        product.setQuantity(Long.valueOf(1));
                        Long quantity = product.getQuantity();
                        Product newProduct = new Product(id, thumbnail, name, unitPrice, quantity);
                        productManager.add(newProduct);
                        Toast.makeText(context, "Added successfully!", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
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
                ivImage.setImageBitmap(bitmap);
            }
        }
    }


}
