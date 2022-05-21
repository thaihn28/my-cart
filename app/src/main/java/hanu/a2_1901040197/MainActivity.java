package hanu.a2_1901040197;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hanu.a2_1901040197.fragment.CartFragment;
import hanu.a2_1901040197.fragment.ProductFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        Fragment productFragment = new ProductFragment();

        manager.beginTransaction()
                .add(R.id.fragmentContainer, productFragment, "fragProduct")
                .addToBackStack("fragment_product")
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Click on cart button on Menu bar
        switch (item.getItemId()){
            case R.id.btnCart:
                Fragment cartFragment = new CartFragment();

                manager.beginTransaction()
                        .replace(R.id.fragmentContainer, cartFragment, "fragCart")
                        .commit();
                break;
            case R.id.btnBack:
                Fragment productFragment = new ProductFragment();
                manager.beginTransaction()
                        .replace(R.id.fragmentContainer, productFragment)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment productFragment = new ProductFragment();
        manager.beginTransaction()
                .replace(R.id.fragmentContainer, productFragment)
                .commit();
//        manager.popBackStack("fragment_product", 0);
    }
}