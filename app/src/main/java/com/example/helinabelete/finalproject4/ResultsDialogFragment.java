package com.example.helinabelete.finalproject4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;

import com.example.helinabelete.finalproject4.QuizViewModel;

public class ResultsDialogFragment extends DialogFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final QuizViewModel quizViewModel = ViewModelProviders.of(getActivity()).get(QuizViewModel.class);
        int totalGuesses = quizViewModel.getTotalGuesses();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(
                //getString(R.string.results, totalGuesses, (1000 / (double) totalGuesses))); //show total guesses? Result: correctAnswers/10
                getString(R.string.results, totalGuesses, (1000 / (double) totalGuesses))); //quizViewModel.getCorrectAnswers(),/11101


        builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    QuizFragment quizFragment = (QuizFragment) getParentFragment();
                    try{
                        quizFragment.resetQuiz();
                    }catch (Exception e){
                        Log.e(quizViewModel.getTag(),"Unable to call resetQuiz()", e);
                    }
                }
                catch (Exception e){
                    Log.e(quizViewModel.getTag(),"Unable to get ActivityMainFragment", e);
                }
            }
        });
        builder.setNegativeButton(R.string.see_details, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    ResultsDialogFragment.this.getActivity().finish();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }
                catch (Exception e){
                    Log.e(quizViewModel.getTag(),"Unable to open MainActivity", e);
                }

            }
        });

        return builder.create();
    }

}