package com.example.projectprm392.common;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.R;
import com.example.projectprm392.controller.CartActivity;
import com.example.projectprm392.controller.HomeActivity;
import com.example.projectprm392.controller.ProductDetailActivity;
import com.example.projectprm392.controller.ProfileActivity;
import com.example.projectprm392.controller.ShopActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHandler {

    public static void setupBottomNavigation(BottomNavigationView bottomNavigationView, Context context) {
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            Intent intent = null;
            if (menuItem.getItemId() == R.id.navigation_home) {
                if (!(context instanceof HomeActivity)) {
                    intent = new Intent(context, HomeActivity.class);
                }
            } else if (menuItem.getItemId() == R.id.navigation_shop) {
                if (!(context instanceof ShopActivity)) {
                    intent = new Intent(context, ShopActivity.class);
                }

            } else if (menuItem.getItemId() == R.id.navigation_cart) {
                if (!(context instanceof CartActivity)) {
                    intent = new Intent(context, CartActivity.class);
                }
            } else if (menuItem.getItemId() == R.id.navigation_profile) {
                if (!(context instanceof ProfileActivity)) {
                    intent = new Intent(context, ProfileActivity.class);
                }
            }
            if (intent != null) {
                context.startActivity(intent);
            }


            return true;
        });
    }
}
