package com.darkndev.everkeepcompose.ui.home.composable

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun NoteSearchBar(
    queryText: String,
    onQueryTextChange: (String) -> Unit,
    closeSearchOnClick: () -> Unit,
    clearTextOnClick: () -> Unit
) {
    TopAppBar(
        title = {
            NoteSearchTextField(
                value = queryText,
                onValueChange = onQueryTextChange,
                hintText = "Search Notes",
                singleLine = true
            )
        },
        navigationIcon = {
            IconButton(onClick = closeSearchOnClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close Search Bar"
                )
            }
        },
        actions = {
            IconButton(onClick = clearTextOnClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Search Text"
                )
            }
        }
    )
}

@Composable
fun NoteSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hintText: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    BasicTextField(
        value = value,
        modifier = modifier,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        decorationBox = @Composable { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = hintText,
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    style = textStyle
                )
            }
            innerTextField()
        }
    )
}