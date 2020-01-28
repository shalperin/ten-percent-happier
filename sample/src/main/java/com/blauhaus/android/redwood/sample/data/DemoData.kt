package com.blauhaus.android.redwood.sample.data

import com.blauhaus.android.redwood.sample.R

val meditationHistoryData= listOf(
    Pair(5f, "5 min @ Jan 1"),
    Pair(10f, "10 min @ Jan 2"),
    Pair(2f, "2 min @ Jan 3"),
    Pair(30f, "30 min @ Jan 4"),
    Pair(15f, "15 min @ Jan 5"),
    Pair(12f, "12 min @ Jan 6"),
    Pair(5f, "5 min @ Jan 7"),
    Pair(25f, "25 min @ Jan 8"),
    Pair(0f, "0 min @ Jan 9"),
    Pair(0f, "0 min @ Jan 10"),
    Pair(15f, "15 min @ Jan 11"),
    Pair(15f, "15 min @ Jan 12"),
    Pair(25f, "25 min @ Jan 13"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(0f, "0 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(20f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(0f, "0 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(0f, "0 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(20f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14"),
    Pair(35f, "35 min @ Jan 14")
    )

val myCircleData = listOf(
    Pair("Dan", meditationHistoryData.subList(2, 8)),
    Pair("Alexis", meditationHistoryData.subList(4,14))
)

val globalStatsData = listOf(54580, 12707803, 19) //[participants, minutes, average minutes per session]


val currentSessionData = Content.Meditation(
    image = R.drawable.josephgoldstein,
    title  = "Meditate With Joseph",
    pretitle = "UP NEXT",
    description = "A beginners mindfulness medition focusing on breath following with Joseph Goldstein.  Joseph Goldstein is founder of the Insight Meditation Society in Barre Mass.",
    audioId = 1,
    videoId = 1
    )

val challengeUpdateData = Content.Update(
    image = R.drawable.dog_party_hat,
    title = "The challenge is over, but you just got started!",
    pretitle = "CONGRATS",
    description = "Great job in working through the meditation challenge.  Hopefully this is a jumping off point for your meditation practice, and that you will keep finding this app useful.  Thanks for participating!"
)

val videoData = listOf("https://youtu.be/O44d1U7aYAo")
val audioData = listOf(R.raw.goldstein_audio)
