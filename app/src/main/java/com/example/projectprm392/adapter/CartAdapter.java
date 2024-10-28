package com.example.projectprm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.entity.CartItem;
import com.example.projectprm392.entity.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItemList;
    private Context context;
    private OnCartUpdatedListener listener;

    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartUpdatedListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public TextView quantity;
        public Button increaseButton;
        public Button decreaseButton;
        public Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productName = itemView.findViewById(R.id.text_product_name);
            productPrice = itemView.findViewById(R.id.text_product_price);
            quantity = itemView.findViewById(R.id.text_quantity);
            increaseButton = itemView.findViewById(R.id.button_increase);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
            removeButton = itemView.findViewById(R.id.button_remove);

        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Product product = cartItem.getProduct();

        holder.productName.setText(product.getProductName());
        double productPrice = product.getProductPrice();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(productPrice) + " VNĐ";
        holder.productPrice.setText(formattedPrice);
        holder.quantity.setText("Quantity: " + String.valueOf(cartItem.getQuantity()));

        Picasso.get().load(product.getProductImage()).into(holder.productImage);

        holder.increaseButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.quantity.setText("Quantity: " + cartItem.getQuantity());
            listener.onCartUpdated();
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.quantity.setText("Quantity: " + cartItem.getQuantity());
                listener.onCartUpdated();
            }
        });
        holder.removeButton.setOnClickListener(v -> {
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
    public double getTotalCartPrice() {
        double total = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getProduct().getProductPrice() * cartItem.getQuantity();
        }
        return total;
    }
}
