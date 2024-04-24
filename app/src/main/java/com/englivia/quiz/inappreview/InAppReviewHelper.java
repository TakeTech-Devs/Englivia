package com.englivia.quiz.inappreview;

import static com.englivia.quiz.activity.TournamentPlay.activity;



import android.app.Activity;
import android.util.Log;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.tasks.Task;

public class InAppReviewHelper {

    public static void askUserForReview(Activity activity) {
        try {
            ReviewManager manager = ReviewManagerFactory.create(activity);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) {
                        // We can get the ReviewInfo object
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                        flow.addOnCompleteListener(task2 -> {
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown.
                            // Handle the result as needed.
                            Log.i("InAppReview", "Review flow completed.");
                        }).addOnFailureListener(e -> {
                            // There was some problem, continue regardless of the result.
                            Log.e("InAppReview", "Failed to launch review flow", e);
                        });
                    } else {
                        // There was some problem, continue regardless of the result.
                        String reviewErrorCode = ((ReviewException) task.getException()).getMessage();
                        Log.e("InAppReview", "Failed to request review flow: " + reviewErrorCode);
                    }
                } catch (Exception ex) {
                    Log.e("InAppReview", "Exception in requestReviewFlow onComplete", ex);
                }
            });
        } catch (Exception e) {
            Log.e("InAppReview", "Exception in askUserForReview", e);
        }
    }
}
