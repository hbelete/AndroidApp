package com.example.helinabelete.finalproject4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder> {

    private List<Countries> countries;
    private TextToSpeech tts;
    private String temp2;

    private Context mContext;  //need an activity context go over this in class

    //public  boolean isExpanded=false;

    public CardViewDataAdapter(List<Countries> countries) {
        this.countries = countries;

    }

    // Create new views
    @Override
    public CardViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        mContext = parent.getContext();  //need an activity context go over this in class

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {  //note final
        final int pos = position;
        Countries coun = countries.get(position);
        viewHolder.tvName.setText(countries.get(pos).getName().toString());
        viewHolder.tvName.setText(coun.getName());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/" + viewHolder.tvName.getText().toString()));
                mContext.startActivity(intent);


            }
        });

        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countries.get(pos);
                //fred = items.get(index);
                //Toast toast = Toast.makeText(mContext, "Mag :" + items.get(index).getMagnitude(), Toast.LENGTH_SHORT);
                //toast.show();
                Intent map = new Intent(mContext, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("countries",countries.get(pos));
                map.putExtras(bundle);
                //String m = "latlon" + countries.get(pos).getLat() + " " + countries.get(pos).getLon();
                mContext.startActivity(map);
                }
        });

        /*viewHolder.button3.setOnClickListener(new View.OnClickListener() { //show saved countries
            @Override
            public void onClick(View view) {
                countries.get(pos);
                sCountry cnt = new sCountry();
                System.out.println(viewHolder.tvName.getText().toString());
                System.out.println(viewHolder.viewNativeName.getText().toString());
                System.out.println(viewHolder.viewRegion.getText().toString());
                System.out.println(viewHolder.viewCurrency.getText().toString());
                    //System.out.println(cnt.getCountry());
                    //System.out.println(cnt.getNativeName());
                    //System.out.println(cnt.getRegion());
                    //System.out.println(cnt.getCurrency());

                cnt = new sCountry(viewHolder.viewNativeName.getText().toString(),
                        viewHolder.viewRegion.getText().toString(), viewHolder.viewCurrency.getText().toString());

                //cnt.getCountry();
                //sCountryDatabase.getsCountryDao().deleteNote(notes.get(pos));

               // String name = null;
                //String region = null;
                //String currency = null;

                //List<sCountry> contactList = new ArrayList();
                //sCountry contactListItem;
                //contactListItem = new sCountry();
                //name = country.get(pos).getName();
                //country.setName(name);
                //Log.e("database country name", name);
                //country.getNativeName();
                //country.getRegion();
                //country.getCurrency();*/

                //country.setName(contactListItem);
               // country.setNativeName(nativeNmae);
                //country.setRegion(region);
                //country.setCurrency(currency);

                //add event into the database
                //sCountryDatabase.getsCountryDatabase(get.addCountry(country.get(pos));




        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //this is for expanding recyclerview
                if (viewHolder.expanded_text.getVisibility()  == View.VISIBLE)
                {
                    viewHolder.expanded_text.setVisibility(View.INVISIBLE); //this is used for shrinking
                    viewHolder.viewRegion.setVisibility(View.INVISIBLE);
                    viewHolder.txt_region.setVisibility(View.INVISIBLE);
                    viewHolder.txt_currency.setVisibility(View.INVISIBLE);
                    viewHolder.viewCurrency.setVisibility(View.INVISIBLE);
                    viewHolder.txt_nativeName.setVisibility(View.INVISIBLE);
                    viewHolder.viewNativeName.setVisibility(View.INVISIBLE);
                    viewHolder.button.setVisibility(View.INVISIBLE);
                    viewHolder.button2.setVisibility(View.INVISIBLE);
                   // viewHolder.button3.setVisibility(View.INVISIBLE);
                    viewHolder.expanded_text.setEnabled(false);
                    viewHolder.expanded_text.setText("");

                }
                else
                {
                    viewHolder.expanded_text.setVisibility(View.VISIBLE);  //this is used for expanding
                    viewHolder.viewRegion.setVisibility(View.VISIBLE);
                    viewHolder.txt_region.setVisibility(View.VISIBLE);
                    viewHolder.txt_currency.setVisibility(View.VISIBLE);
                    viewHolder.viewCurrency.setVisibility(View.VISIBLE);
                    viewHolder.txt_nativeName.setVisibility(View.VISIBLE);
                    viewHolder.viewNativeName.setVisibility(View.VISIBLE);
                    viewHolder.button.setVisibility(View.VISIBLE);
                    viewHolder.button2.setVisibility(View.VISIBLE);
                 //   viewHolder.button3.setVisibility(View.VISIBLE);
                    viewHolder.expanded_text.setEnabled(true);
                    viewHolder.viewCurrency.setText(countries.get(position).getCurrency());
                    viewHolder.viewRegion.setText(countries.get(position).getRegion());
                    viewHolder.viewNativeName.setText(countries.get(position).getNativeName());

                }


            }
        });



    }


    @Override
    public int getItemCount() {  // Return the size arraylist
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{

        public TextView tvName;
        //public TextView tvTitle;
        public LinearLayout hidden_layout;
        public TextView expanded_text;
        public TextView txt_region;
        public TextView viewRegion;
        public TextView txt_currency;
        public TextView viewCurrency;
        public TextView txt_nativeName;
        public TextView viewNativeName;
        public Button button;
        public Button button2;

      //  public Button button3;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.questionPosition);
            //tvTitle = (TextView) itemLayoutView.findViewById(R.id.txt_Title);

            hidden_layout=(LinearLayout)itemView.findViewById(R.id.hidden_layout);
            expanded_text=(TextView)itemView.findViewById(R.id.expanded_text);
            txt_region = (TextView) itemView.findViewById(R.id.region_label);
            viewRegion = (TextView) itemView.findViewById(R.id.viewRegion);
            txt_currency = (TextView) itemView.findViewById(R.id.currency_label);
            viewCurrency = (TextView) itemView.findViewById(R.id.viewCurrency);
            txt_nativeName = (TextView) itemView.findViewById(R.id.nName_label);
            viewNativeName = (TextView) itemView.findViewById(R.id.viewNativeName);
            button = (Button) itemView.findViewById(R.id.btn_learnMore);
            button2 = (Button) itemView.findViewById(R.id.btn_Map);

      //      button3 = (Button) itemView.findViewById(R.id.btn_saveCountry);
        }
    }

    public void texttospeech(int pos) {
        temp2 = countries.get(pos).getName();
        tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() { //need an activity context go over this in class
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                    tts.speak(temp2, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return countries.get(id).getTitle();
    }

    // method to access in activity after updating selection
    public List<Countries> getPhraseList() {
        return countries;
    }
}

