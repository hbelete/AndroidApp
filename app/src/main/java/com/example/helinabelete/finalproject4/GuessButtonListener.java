package com.example.helinabelete.finalproject4;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.example.helinabelete.finalproject4.QuizFragment;
import com.example.helinabelete.finalproject4.R;
//import com.nadershamma.apps.androidfunwithflags.ResultsDialogFragment;
import com.example.helinabelete.finalproject4.QuizViewModel;

public class GuessButtonListener implements OnClickListener {
    private QuizFragment mainActivityFragment;
    private Handler handler;

    public GuessButtonListener(QuizFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
        this.handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        String answer = this.mainActivityFragment.getQuizViewModel().getCorrectCountryName();
        this.mainActivityFragment.getQuizViewModel().setTotalGuesses(1);

        if (guess.equals(answer)) {
            this.mainActivityFragment.getQuizViewModel().setCorrectAnswers(1);
            this.mainActivityFragment.getAnswerTextView().setText("Correct!");
            this.mainActivityFragment.getAnswerTextView().setTextColor(
                    this.mainActivityFragment.getResources().getColor(R.color.green));

            this.mainActivityFragment.disableButtons();

            if (this.mainActivityFragment.getQuizViewModel().getCorrectAnswers()
                    == QuizViewModel.getFlagsInQuiz()) {
                ResultsDialogFragment quizResults = new ResultsDialogFragment();
                quizResults.setCancelable(false);
                try {
                    quizResults.show(this.mainActivityFragment.getChildFragmentManager(), "Quiz Results");
                } catch (NullPointerException e) {
                    Log.e(QuizViewModel.getTag(),
                            "GuessButtonListener: this.mainActivityFragment.getFragmentManager() " +
                                    "returned null",
                            e);
                }
            } else {
                this.handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainActivityFragment.animate(true);
                            }
                        }, 1500);
            }
        } else {
            this.mainActivityFragment.incorrectAnswerAnimation();
            guessButton.setEnabled(false);
        }
    }
}
