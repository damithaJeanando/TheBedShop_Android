package com.example.thebedshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thebedshop.Models.Category;
import com.example.thebedshop.R;

public class CategoryListAdapter extends ArrayAdapter<Category> {

        Context context;
        Category[] categories;
        int resourceLayout;
        String url = "http://10.0.3.2:8080/";

public CategoryListAdapter(Context context, int resource,  Category[] categories) {
        super(context, resource, categories);
        this.context = context;
        this.categories = categories;
        this.resourceLayout = resource;

        }

private class CatItemHolder {
    TextView category;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category =  getItem(position);

        final CatItemHolder catItemHolder;

        final View view;



        if(convertView == null) {

            catItemHolder = new CatItemHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resourceLayout, parent, false);
            catItemHolder.category = convertView.findViewById(R.id.cat_name);

            view = convertView;

            convertView.setTag(catItemHolder);
        }
        else {
            catItemHolder = (CatItemHolder) convertView.getTag();
            view = convertView;
        }
        catItemHolder.category.setText(category.categoryName);

        return view;
    }
}

