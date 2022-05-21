package hanu.a2_1901040197.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1901040197.models.Product;


public class ProductManager {
    // singleton
    private static ProductManager instance;
    private static final String INSERT_STMT =
            "INSERT INTO products (id, thumbnail, name, unitPrice, quantity) VALUES (?, ?, ?, ?, ?)";

    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public static ProductManager getInstance(Context context){
        if(instance == null){
            instance = new ProductManager(context);
        }
        return instance;
    }

    public ProductManager(Context context){
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // get all Products
    public List<Product> allProducts(){
        Cursor results = database.rawQuery("SELECT * FROM products", null);
        ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(results);
        List<Product> productList = productCursorWrapper.getAllProducts();

        results.close();
        return productList;
    }
    // add
    public boolean add(Product product){
        SQLiteStatement statement = database.compileStatement(INSERT_STMT);

        statement.bindLong(1, product.getId());
        statement.bindString(2, product.getThumbnail());
        statement.bindString(3, product.getName());
        statement.bindLong(4, product.getUnitPrice());
        statement.bindDouble(5, product.getQuantity());

        long id = statement.executeInsert();
        if(id > 0){
            product.setId(id);
            return true;
        }
        return false;
    }

    // update
    public boolean update(Long quantity, Long id){
        String query = "UPDATE products SET quantity = '" + quantity + "' WHERE id = '" + id + "'";
        database.execSQL(query);
        return true;
    }

    // delete
    public boolean delete(long id) {
        int result = database.delete("products", "id = ?", new String[]{ id + "" });
        return result > 0;
    }

}
