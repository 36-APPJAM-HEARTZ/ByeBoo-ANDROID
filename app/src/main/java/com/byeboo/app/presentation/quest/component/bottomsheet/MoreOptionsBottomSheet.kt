package com.byeboo.app.presentation.quest.component.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.type.PostOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: PostOption> MoreOptionsBottomSheet(
    topOption: T,
    bottomOption: T,
    onOptionClick: (T) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean = false,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            containerColor = ByeBooTheme.colors.gray900,
            scrimColor = ByeBooTheme.colors.blackAlpha80,
            dragHandle = null,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(24.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ByeBooDragHandle()

                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .noRippleClickable { onOptionClick(topOption) },
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp))
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(topOption.optionIcon),
                        contentDescription = null,
                        tint = ByeBooTheme.colors.white,
                    )

                    Text(
                        text = topOption.optionTitle,
                        style = ByeBooTheme.typography.body3,
                        color = ByeBooTheme.colors.white,
                    )
                }

                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(screenHeightDp( 20.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.gray800,
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .noRippleClickable { onOptionClick(bottomOption) },
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp))

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(bottomOption.optionIcon),
                        contentDescription = null,
                        tint = ByeBooTheme.colors.error300,
                    )

                    Text(
                        text = bottomOption.optionTitle,
                        style = ByeBooTheme.typography.body3,
                        color = ByeBooTheme.colors.error300,
                    )
                }

                Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

                ByeBooButton(
                    buttonText = "닫기",
                    buttonStyle = ByeBooTheme.typography.body3,
                    buttonTextColor = ByeBooTheme.colors.gray300,
                    buttonBackgroundColor = ByeBooTheme.colors.whiteAlpha5,
                    onClick = onDismissRequest,
                    modifier = Modifier.padding(bottom = screenHeightDp(10.dp)),
                )
            }
        }
    }
}
