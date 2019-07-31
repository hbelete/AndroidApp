package com.example.helinabelete.finalproject4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.EventViewHolder> {
    private Context context;
    private List<Note> items;
    private RecyclerTouchListener.ClickListener clicklistener;
    private ClickListener mClickListener;

    public NoteAdapter(List<Note> items, Context context) {
        this.items = items;
        this.context = context;

    }

    public class EventViewHolder extends RecyclerView.ViewHolder  { //implements View.OnClickListener, View.OnLongClickListener
        //public LinearLayout hidden_layout;
        TextView titleTextView;
        TextView descriptionTextView;

        EventViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.tv_title);
            //dateTextView = (TextView) v.findViewById(R.id.tv_timestamp);
            descriptionTextView = (TextView) v.findViewById(R.id.tv_discription);
            //hidden_layout=(LinearLayout)itemView.findViewById(R.id.hidden_layout);

            // v.setOnClickListener(this);
            //v.setOnLongClickListener(this);
        }

       /* @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null) mClickListener.onLongClick(view, getAdapterPosition());
            return false;
        }*/
    }

    @Override
    public NoteAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        EventViewHolder holder = new EventViewHolder(v);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        final int index = position;
        final Note item = items.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
//        holder.dateTextView.setText(item.getDate());
        /*holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                items.get(index);
                Toast toast = Toast.makeText(context, "You just clicked on:" + items.get(index).toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                items.get(index);
                MainActivity.showActionsDialog(index);
                return true;
            }
        });
  */
    }


    @Override
    public int getItemCount() {

        return items.size();
    }
    // allows clicks events to be caught
    void setClickListener(ClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}