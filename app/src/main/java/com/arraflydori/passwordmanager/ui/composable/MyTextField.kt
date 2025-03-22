package com.arraflydori.passwordmanager.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.unit.dp

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    singleLine: Boolean = true,
    trailing: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = None
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(percent = 40)
                    )
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    innerTextField()
                    if (value.isEmpty()) {
                        Text(
                            hint,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
                trailing()
            }
        },
    )
}
