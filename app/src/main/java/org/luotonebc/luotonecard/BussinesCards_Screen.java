package org.luotonebc.luotonecard;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.luotonebc.luotonecard.R.*;

public class BussinesCards_Screen extends AppCompatActivity  implements SearchView.OnQueryTextListener{

    public ArrayList<BussinessCardDetails> card_details = new ArrayList<BussinessCardDetails>();
    public customListViewAdapter customListViewAdapter;
    public ArrayAdapter<String> spinnerAdapter;
    private SearchView search_cardInfo;

    private static final String[] paths_spinner = {"A-Z", "Order"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_bussines_cards__screen);
        Toolbar toolbar = (Toolbar) findViewById(id.toolbar_contacts);
       setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        search_cardInfo = findViewById(id.search_textField);

        ListView listView = findViewById(id.bussinescardView);
        //sets customview adapter and fills it with
        LoadData();

        Spinner spinner = findViewById(id.SortOptionsSpinner);
        spinnerAdapter = new ArrayAdapter<String>(BussinesCards_Screen.this,android.R.layout.simple_spinner_dropdown_item
        , paths_spinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //sorts card_detail ArrayList in Alphabetical order
                        synchronized (this) {
                            Collections.sort(card_details, new Comparator<BussinessCardDetails>() {
                                @Override
                                public int compare(BussinessCardDetails o1, BussinessCardDetails o2) {
                                    BussinessCardDetails name1 = o1;
                                    BussinessCardDetails name2 = o2;
                                    return name1.getName().compareToIgnoreCase(name2.getName());
                                }
                            });
                            customListViewAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 1:
                        //sorts card_detail ArrayList in actual order
                        synchronized (this) {
                            Collections.sort(card_details, new Comparator<BussinessCardDetails>() {
                                @Override
                                public int compare(BussinessCardDetails o1, BussinessCardDetails o2) {
                                    BussinessCardDetails name1 = o2;
                                    BussinessCardDetails name2 = o1;
                                    return name1.getName().compareToIgnoreCase(name2.getName());
                                }
                            });
                            customListViewAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //automaticaly added function
                // add parameters in case you want something to happen while nothing is selected
                // from the spinner dropdown list
            }
        });

        customListViewAdapter = new customListViewAdapter(BussinesCards_Screen.this,card_details);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(customListViewAdapter);
        setListViewHeightBasedOnChildren(listView);
        search_cardInfo.setOnQueryTextListener(this);
    }

    private void LoadData() {
        // test values for the list View
        // replace with actual app values
        BussinessCardDetails bussinessCardDetails = new BussinessCardDetails("Steve","Hello", drawable.icon_for_profile);
        BussinessCardDetails bussinessCardDetails2 = new BussinessCardDetails("Michael","Hello",drawable.icon_for_profile);
        BussinessCardDetails bussinessCardDetails3 = new BussinessCardDetails("Jordan","Hello", drawable.icon_for_profile);
        card_details.add(bussinessCardDetails);
        card_details.add(bussinessCardDetails2);
        card_details.add(bussinessCardDetails3);

    }
    //scretches list view items on nested scrollview
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bussines_cards__screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case id.contacts_to_profile:
                Intent profile_intent = new Intent(BussinesCards_Screen.this,ProfileScreen.class);
                startActivity(profile_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //automatically created quary function that reacts to submit
        //from searchview
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        synchronized (this) {
            customListViewAdapter.getFilter().filter(newText);
            notifyAll();
        }
        return false;
    }


    class customListViewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<BussinessCardDetails> cardDetails;
        private Activity context;
        private LayoutInflater inflater;
        private CardFilter filter;
        private ArrayList<BussinessCardDetails> stringFilter;

        public customListViewAdapter(Activity context, ArrayList<BussinessCardDetails> card_details) {
            super();
            this.context = context;
            this.cardDetails = card_details;
            stringFilter = card_details;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            //gets the count of objects and sets that amount of values to list view
            return cardDetails.size();
        }

        @Override
        public Object getItem(int position) {

            return cardDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView_name, textView_description;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(layout.customlistlayout, null);
                holder.textView_name = convertView.findViewById(id.Name_cardtext);
                holder.textView_description = convertView.findViewById(id.descriptionText);
                holder.imageView = convertView.findViewById(id.imageView_cards);
                convertView.setTag(holder);
            } else holder = (ViewHolder) convertView.getTag();

            holder.textView_name.setText(this.cardDetails.get(position).getName());
            holder.textView_description.setText(this.cardDetails.get(position).getDescription());
            holder.imageView.setImageResource(card_details.get(position).getPicture());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (this.filter == null) {
                filter = new CardFilter();
            }
            return filter;
        }

        class CardFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<BussinessCardDetails> filterList = new ArrayList<BussinessCardDetails>();
                    synchronized (this) {
                        for (int i = 0; i < stringFilter.size(); i++) {
                            if ((stringFilter.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                                BussinessCardDetails bussinessCardDetails = new BussinessCardDetails(stringFilter.get(i)
                                        .getName(), stringFilter.get(i).getDescription(), stringFilter.get(i).getPicture());
                                filterList.add(bussinessCardDetails);
                            }
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    synchronized (this) {
                        results.count = stringFilter.size();
                        results.values = stringFilter;
                    }
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                  if (results.count == 0) notifyDataSetChanged();
                  else {
                      customListViewAdapter.cardDetails = (ArrayList<BussinessCardDetails>) results.values;
                      // card_details = (ArrayList<BussinessCardDetails>) results.values;
                      customListViewAdapter.this.notifyDataSetChanged();
                  }
            }
        }
    }

    public class BussinessCardDetails{
        String contact_Name;
        String Description;
        int picture;
        public BussinessCardDetails(String contact_Name, String Description, int picture){
            super();
            this.contact_Name = contact_Name;
            this.Description = Description;
            this.picture = picture;
        }
        public String getName(){
            return contact_Name;
        }
        public void setName(String contact_Name){
            this.contact_Name = contact_Name;
        }
        public String getDescription(){
            return Description;
        }
        public void setDescription(String description){
            this.Description = description;
        }
        public int getPicture(){
            return picture;
        }
        public void setPicture(int picture){
           this.picture = picture;
        }
    }
}
