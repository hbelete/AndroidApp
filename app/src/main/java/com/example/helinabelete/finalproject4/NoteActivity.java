package com.example.helinabelete.finalproject4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NoteActivity extends AppCompatActivity implements NoteAdapter.ClickListener{

    RecyclerView recyclerView;
    TextView noNotesFound;
    FloatingActionButton fab;
    NoteAdapter adapter;
    List<Note> notes = new ArrayList<>();
    private static TextView tv_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room2);
        noNotesFound = (TextView) findViewById(R.id.tv_no_events_found);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_list_events);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //show list of events if exists in database
        new DatabaseAsync(NoteActivity.this).execute(null, -1, null, null);  //MainActivity.this explain this usage , null

        //shows NO EVENTS FOUND when list is empty
        checkListEmptyOrNot();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                //show Alertdialog to edit or update the event
                showActionsDialog(position);
            }
        }));


        //add listener on FloatingActionButton to add event
        fab = (FloatingActionButton) findViewById(R.id.fab_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adding  new event
                showNoteDialog(false, null, -1);
            }
        });
    }


    @Override
    public void onClick(View view, int position) {
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
       // Toast toast = Toast.makeText(this, "You just clicked on:" + events.get(position).toString(), Toast.LENGTH_SHORT);
       // toast.show();

    }

    @Override
    public void onLongClick(View view, int position) {
        //show Alertdialog to edit or update the
        //USE THIS ONLY IF NOT USING RecyclerTouchListener
        //showActionsDialog(position);
    }

    private class DatabaseAsync extends AsyncTask<Object, Void, List<Note>> {
        public NoteAdapter.ClickListener c;  //need to get main

        // Constructor providing a reference to the views in MainActivity
        public DatabaseAsync(NoteAdapter.ClickListener  c) {
            this.c = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Note> doInBackground(Object... params) {

            Boolean shouldUpdate = (Boolean) params[0];
            int position = (int) params[1];
            String title = (String) params[2];
            String detail = (String) params[3];
            //String date = (String) params[4];

            //check whether to add add or update event based on if shouldUpdate is null
            if (shouldUpdate != null) {
                //update event if shouldUpdate is true
                if (shouldUpdate) {
                    Note event = notes.get(position);
                    event.setTitle(title);
                    event.setDescription(detail);
                    //event.setDate(date);

                    //update event into the database
                    NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().updateNote(event);

                } else {
                    //add event if shouldUpdate is false
                    Note note = new Note();
                    note.setTitle(title);
                    note.setDescription(detail);
                    //event.setDate(date);


                    //add event into the database
                    NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().addNote(note);
                }

            } else { //so no update since shouldUpdate == null
                //delete all if postion is = -2, really bad, i should fix this
                if (position == -2)
                    //delete all events  from database
                    NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().dropTheTable();

                //delete event
                else if (position != -1) { //-1 means delete a specific event
                    Note note = notes.get(position);

                    //delete event from database
                    NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().deleteNote(note);
                }
            }

            //get events from database, also not a great way to do this
            List<Note> notes = NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().getNote();
            return notes;

        }

        @Override
        protected void onPostExecute(List<Note> items) {

            //get list of events from doInBackground()
            notes = items;

            adapter = new NoteAdapter(notes, getApplicationContext());
            adapter.setClickListener(c); //this is important since need MainActivity.this

            recyclerView.setAdapter(adapter);

            //shows NO EVENTS FOUND when list is empty
            checkListEmptyOrNot();
        }
    }

    public void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {

                    //show Alertdialog to update the event
                    showNoteDialog(true, notes.get(position), position);
                } else {

                    //delete event from database
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    private void deleteNote(int position) {

        new DatabaseAsync(NoteActivity.this).execute(null, position, null, null); //, null
    }

    private void showNoteDialog(final Boolean shouldUpdate, final Note note, final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(view);

        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);

        final EditText edt_note = (EditText) view.findViewById(R.id.edt_note);

        final EditText edt_details = (EditText) view.findViewById(R.id.edt_details);



        dialog_title.setText(!shouldUpdate ? "New Note" : "Edit Note");


        if (shouldUpdate && note != null) {
            edt_note.setText(note.getTitle());
            edt_details.setText(note.getDescription());
//            tv_date.setText(note.getDate());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(edt_note.getText().toString()) && !TextUtils.isEmpty(edt_details.getText().toString())) {
                    Toast.makeText(NoteActivity.this, "Enter a Note!", Toast.LENGTH_SHORT).show();
                }
                else if (!TextUtils.isEmpty(edt_note.getText().toString()) && TextUtils.isEmpty(edt_details.getText().toString())) {
                    Toast.makeText(NoteActivity.this, "Enter Details!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(edt_note.getText().toString()) && TextUtils.isEmpty(edt_details.getText().toString())) {
                    Toast.makeText(NoteActivity.this, "Enter Note and Details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    alertDialog.dismiss();
                }

                //Update or add data into the database only when both field are  filled(i.e title,description)
                if (!TextUtils.isEmpty(edt_note.getText().toString()) && !TextUtils.isEmpty(edt_details.getText().toString())) {

                    // check if user updating note

                    if (shouldUpdate && note != null) {
                        // update event
                        new DatabaseAsync(NoteActivity.this).execute(shouldUpdate, position, edt_note.getText().toString(), edt_details.getText().toString()); //, tv_date.getText().toString()


                    } else {
                        // create new event
                        new DatabaseAsync(NoteActivity.this).execute(shouldUpdate, -1, edt_note.getText().toString(), edt_details.getText().toString()); //, tv_date.getText().toString()


                    }
                }
            }
        });


    }

    /*public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        /ublic void onDateSet(DatePicker view, int year, int month, int day) {
            String monthVal, dayVal = "" + day;
            if ((month + 1) < 10) {
                month += 1;
                monthVal = "0" + month;
            } else {
                month += 1;
                monthVal = "" + month;
            }
            if (day < 10) {
                dayVal = "0" + day;
            }
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(year, month, day - 3);
            String dayOfWeek = simpledateformat.format(date);


            tv_date.setText(dayOfWeek + "\n" + dayVal + "-" + monthVal + "-" + year);

        }
    }

    private void showDateDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }*/

    public void checkListEmptyOrNot() {
        if (notes.isEmpty())
            noNotesFound.setVisibility(View.VISIBLE);
        else
            noNotesFound.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.add:
                // do stuff here
                //adding  new event
                showNoteDialog(false, null, -1);
                return true;
            case R.id.deleteAll:
                // do stuff here
                new DatabaseAsync(NoteActivity.this).execute(null, -2, null, null);  //MainActivity.this explain this usage , null
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}