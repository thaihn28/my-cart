package hanu.a2_1901040197.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040197.models.Product;


public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()){
            Product product = getProduct();
            productList.add(product);

            moveToNext();
        }
        return productList;
    }

    public Product getProduct(){
        long id = getLong(getColumnIndex("id"));
        String name = getString(getColumnIndex("name"));
        String thumbnail = getString(getColumnIndex("thumbnail"));
        Long unitPrice = getLong(getColumnIndex("unitPrice"));
        Long quantity = getLong(getColumnIndex("quantity"));

        Product product = new Product(id, thumbnail, name, unitPrice, quantity);

        return product;
    }

}
