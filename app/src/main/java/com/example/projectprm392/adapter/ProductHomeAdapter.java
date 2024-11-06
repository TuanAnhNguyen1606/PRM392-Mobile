package com.example.projectprm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectprm392.R;
import com.example.projectprm392.controller.ProductDetailActivity;
import com.example.projectprm392.entity.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductHomeAdapter extends BaseAdapter {

    private Context context;
    private List<Product> products;

    public ProductHomeAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getProductID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Thay đổi layout cho item
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product_home, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.productName = convertView.findViewById(R.id.tv_product_name);
            viewHolder.productPrice = convertView.findViewById(R.id.tv_product_price);
            viewHolder.productImage = convertView.findViewById(R.id.iv_product_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);
        viewHolder.productName.setText(product.getProductName());
        double productPrice = product.getProductPrice();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(productPrice) + " VNĐ";
        viewHolder.productPrice.setText(formattedPrice);
        Picasso.get().load(product.getProductImage()).into(viewHolder.productImage);
        viewHolder.productImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", product.getProductID());
            intent.putExtra("PRODUCT_NAME", product.getProductName());
            intent.putExtra("PRODUCT_PRICE", product.getProductPrice());
            intent.putExtra("PRODUCT_DESCRIPTION", product.getProductDescription());
            intent.putExtra("PRODUCT_IMAGE", product.getProductImage());
            intent.putExtra("PRODUCT_STOCK", product.getProductStock());
            context.startActivity(intent);
        });

        return convertView;
    }
    public void updateProductList(List<Product> newProducts) {
        this.products.clear();
        this.products.addAll(newProducts);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImage;
    }
}
