/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.models

import com.ed.turbowash_android.R
import java.util.UUID

data class OnboardingStep(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val imageURL: Int,
    val tag: Int,
){
    companion object {
        val samplePage = OnboardingStep(
            name = "Welcome to Turbo Washer",
            description = "Join a community where your skills bring convenience and shine to vehicles everywhere. Discover flexible job opportunities that fit your schedule and help you earn more.",
            imageURL = R.drawable.turbowash_logo,
            tag = 0
        )

        val samplePages = listOf(
            OnboardingStep(
                name = "Welcome to Turbo Washer",
                description = "Join a community where your skills bring convenience and shine to vehicles everywhere. Discover flexible job opportunities that fit your schedule and help you earn more.",
                imageURL = R.drawable.turbowash_logo,
                tag = 0
            ),
            OnboardingStep(
                name = "Find Jobs Easily",
                description = "Use CleanRide to quickly find cleaning jobs near you. Our smart matching system connects you with vehicle owners in need, allowing you to plan your workday efficiently.",
                imageURL = R.drawable.turbowash_logo,
                tag = 1
            ),
            OnboardingStep(
                name = "Work on Your Terms",
                description = "You're in control. Choose when and where you work with the freedom to pick the jobs that best fit your life and expertise. Your time, your choice.",
                imageURL = R.drawable.turbowash_logo,
                tag = 2
            ),
            OnboardingStep(
                name = "Get Started Today",
                description = "Ready to make vehicles shine and grow your cleaning business? Sign up now and turn your cleaning skills into opportunities.",
                imageURL = R.drawable.turbowash_logo,
                tag = 3
            )
        )
    }
}
