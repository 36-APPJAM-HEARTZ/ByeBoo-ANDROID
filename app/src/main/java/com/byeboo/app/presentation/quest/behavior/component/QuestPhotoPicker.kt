import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenWidthDp

@Composable
internal fun QuestPhotoPicker(
    imageUrl: Uri?,
    onImageClick: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val uploadedImage = imageUrl != null

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                onImageClick(uri)
            }
        }
    Box(
        modifier =
        modifier
            .width(screenWidthDp(96.dp))
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(color = ByeBooTheme.colors.whiteAlpha10)
    ) {
        ImageUploadButton(
            imageUrl = imageUrl,
            isUploaded = uploadedImage,
            onImageClick = { photoPickerLauncher.launch("image/*") }
        )
    }
}

@Composable
private fun ImageUploadButton(
    modifier: Modifier = Modifier,
    imageUrl: Uri? = null,
    isUploaded: Boolean,
    onImageClick: () -> Unit
) {
    Box(
        modifier =
        modifier
            .width(screenWidthDp(96.dp))
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .noRippleClickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(targetState = isUploaded) { uploaded ->
                if (uploaded && imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "selected image",
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        tint = ByeBooTheme.colors.primary300
                    )
                }
            }
        }
    }
}
