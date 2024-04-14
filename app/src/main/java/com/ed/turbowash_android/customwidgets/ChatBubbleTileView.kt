/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.customwidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ed.turbowash_android.R
import com.ed.turbowash_android.models.Message
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ChatBubbleTileView(
    chatMessage: Message,
    senderName: String,
    senderImage: String,
    customerMessage: Boolean
) {
    fun formatTimestamp(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = timestamp.toDate()
        return sdf.format(date)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        if (customerMessage) {
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            horizontalAlignment = if (customerMessage) Alignment.End else Alignment.Start
        ) {
            Text(
                text = chatMessage.message,
                style = MaterialTheme.typography.body2,
                color = if (customerMessage) colorResource(id = R.color.fadedGray) else colorResource(
                    id = R.color.black
                ),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .background(
                        if (customerMessage) colorResource(id = R.color.turboBlue) else colorResource(
                            id = R.color.fadedGray
                        ),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                if (!customerMessage) {
                    if (senderImage.isNotEmpty()) {
                        val painter = rememberImagePainter(
                            data = senderImage,
                            builder = {
                                crossfade(true)
                                placeholder(drawableResId = R.drawable.user_png)
                                error(R.drawable.user_png)
                            }
                        )
                        Image(
                            painter = painter,
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 25.dp, height = 25.dp)
                                .clip(CircleShape)
                                .clickable { }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user_png),
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 25.dp, height = 25.dp)
                                .clip(CircleShape)
                                .clickable { }
                        )
                    }
                }
                Text(
                    text = senderName,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(
                    text = formatTimestamp(chatMessage.sendTime),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                if (customerMessage) {
                    if (senderImage.isNotEmpty()) {
                        val painter = rememberImagePainter(
                            data = senderImage,
                            builder = {
                                crossfade(true)
                                placeholder(drawableResId = R.drawable.user_png)
                                error(R.drawable.user_png)
                            }
                        )
                        Image(
                            painter = painter,
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 25.dp, height = 25.dp)
                                .clip(CircleShape)
                                .clickable { }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user_png),
                            contentDescription = "User profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 25.dp, height = 25.dp)
                                .clip(CircleShape)
                                .clickable { }
                        )
                    }
                }
            }
        }

        if (!customerMessage) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
