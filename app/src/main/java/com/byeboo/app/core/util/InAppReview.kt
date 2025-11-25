package com.byeboo.app.core.util

import android.app.Activity
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode

fun inAppReview(
    activity: Activity,
) {
    val manager = ReviewManagerFactory.create(activity)

    manager.requestReviewFlow()
        .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            manager.launchReviewFlow(activity, reviewInfo).addOnCompleteListener {

            }
        } else {
            @ReviewErrorCode val reviewErrorCode = (task.exception as ReviewException).errorCode
        }
    }
}
